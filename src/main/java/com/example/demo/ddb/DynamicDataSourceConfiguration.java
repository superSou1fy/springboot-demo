package com.example.demo.ddb;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * 将动态数据源配置到容器
 */
@Configuration
@ConditionalOnProperty(prefix = "dynamic", name = "datasource")
public class DynamicDataSourceConfiguration {

    @Configuration
    @AutoConfigureBefore(CodedConfiguration.class)
    @ConditionalOnProperty(prefix = "dynamic.datasource", name = "config-xml-path")
    @ImportResource(locations = {"${dynamic.datasource.config-xml-path}"})
    static class XmlFileConfiguration {

    }

    @Configuration
    //@ConditionalOnProperty(prefix = "dynamic.datasource", name = "dbs")
    @EnableConfigurationProperties(MutiDataSourceProperties.class)
    static class CodedConfiguration {

        @Bean
        public DataSource dataSource(MutiDataSourceProperties mutiDataSourceProperties) {
            DynamicDataSource dynamicDataSource = new DynamicDataSource();
            Map<Object, Object> targetDts = new HashMap<>();
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
                        clazz = HikariDataSource.class;
                    }
                    ds = dbConfig.initializeDataSourceBuilder().type(clazz).build();
                }
                targetDts.put(dbKey, ds);
            });
            dynamicDataSource.setTargetDataSources(targetDts);
            return dynamicDataSource;
        }
    }
}
