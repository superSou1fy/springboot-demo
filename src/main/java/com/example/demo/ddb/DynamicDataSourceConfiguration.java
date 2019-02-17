package com.example.demo.ddb;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 将动态数据源配置到容器
 */
@Slf4j
@Configuration
@ConditionalOnProperty(prefix = "dynamic", name = "datasource")
public class DynamicDataSourceConfiguration {

    /**
     * 优先加载配置文件中的多数据源配置
     */
    @Configuration
    @AutoConfigureBefore(CodedConfiguration.class)
    @ConditionalOnProperty(prefix = "dynamic.datasource", name = "config-xml-path")
    @ImportResource(locations = {"${dynamic.datasource.config-xml-path}"})
    static class XmlFileConfiguration {
    }

    @Configuration
    @EnableConfigurationProperties(MutiDataSourceProperties.class)
    @ConditionalOnExpression("'${dynamic.datasource.dbs}'.length()>0")
    static class CodedConfiguration {

        @Bean
        public DataSource dataSource(MutiDataSourceProperties mutiDataSourceProperties, BeanFactory beanFactory) {
            DynamicDataSource dynamicDataSource = null;
            try {
                dynamicDataSource = beanFactory.getBean(DynamicDataSource.class);
            } catch (Exception e) {
                log.warn("容器中还未存在dynamicDataSource对象", e);
            }
            //如果之前的xml配置内部没有定义dynamicDataSource对象
            dynamicDataSource = dynamicDataSource == null ? new DynamicDataSource() : dynamicDataSource;
            Map<Object, Object> targetDts = dynamicDataSource.getTargetDataSources();
            // merge from xml
            targetDts = targetDts == null ? new HashMap<>() : targetDts;
            Map<Object, Object> finalTargetDts = targetDts;
            Class defaultClazz = HikariDataSource.class;
            mutiDataSourceProperties.getDbs().forEach(dbConfig -> {
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
                Object oldVal = finalTargetDts.put(dbKey, ds);
                Assert.isTrue(null == oldVal, "存在重复配置的数据源:" + dbKey);
            });
            dynamicDataSource.setTargetDataSources(targetDts);
            return dynamicDataSource;
        }

    }
}
