package com.dyniamic.ddb;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class MergeDataSourceBeanPostProcessor implements BeanPostProcessor, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof DynamicDataSource) {
            log.debug("开始配置多数据源...");
            MutiDataSourceProperties mutiDataSourceProperties = applicationContext.getBean(MutiDataSourceProperties.class);
            List<MutiDataSourceProperties.NamedDataSourceProperty> namedDataSourceProperties = mutiDataSourceProperties.getDbs();
            if (namedDataSourceProperties.size() == 0) {
                return bean;
            }
            DynamicDataSource dynamicDataSource = (DynamicDataSource) bean;
            Map<Object, Object> targetDts = dynamicDataSource.getTargetDataSources();
            targetDts = targetDts == null ? new HashMap<>() : targetDts;
            Map<Object, Object> finalTargetDts = targetDts;
            Class defaultClazz = HikariDataSource.class;
            namedDataSourceProperties.forEach(dbConfig -> {
                Object dbKey = dbConfig.getDbKey();
                DataSource ds;
                if (StringUtils.isNotEmpty(dbConfig.getJndiName())) {
                    JndiDataSourceLookup dataSourceLookup = new JndiDataSourceLookup();
                    dataSourceLookup.setJndiEnvironment(dbConfig.getJndiEnvironment());
                    ds = dataSourceLookup.getDataSource(dbConfig.getJndiName());
                } else {
                    Class<? extends DataSource> clazz = dbConfig.getType();
                    if (null == clazz) {
                        dbConfig.setType(defaultClazz);
                    }
                    ds = dbConfig.initializeDataSourceBuilder().build();
                }
                Assert.isTrue(null == finalTargetDts.put(dbKey, ds), "存在重复配置的数据源:" + dbKey);
            });
            dynamicDataSource.setTargetDataSources(finalTargetDts);
            log.debug("多数据源配置完成，共{}个数据源。", finalTargetDts.size());
        }
        return bean;
    }
}
