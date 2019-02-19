package com.dyniamic.ddb;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.XADataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;

/**
 * 将动态数据源配置到容器
 */
@Slf4j
@Configuration
@AutoConfigureBefore({XADataSourceAutoConfiguration.class, DataSourceAutoConfiguration.class})
@ConditionalOnClass({DataSource.class, EmbeddedDatabaseType.class})
@Import({DynamicDataSourceConfiguration.XmlFileConfiguration.class})
public class DynamicDataSourceConfiguration {

    /**
     * 优先加载配置文件中的多数据源配置
     */
    @ConditionalOnProperty(prefix = "dynamic.datasource", name = "config-xml-path")
    @ImportResource(locations = {"${dynamic.datasource.config-xml-path}"})
    static class XmlFileConfiguration {
    }

    @Bean
    @ConfigurationProperties(prefix = "dynamic.datasource")
    public MutiDataSourceProperties mutiDataSourceProperties() {
        return new MutiDataSourceProperties();
    }

    @Bean
    @Primary
    @ConditionalOnMissingBean(DataSource.class)
    public DynamicDataSource dataSource() {
        DynamicDataSource dynamicDataSource = new DynamicDataSource();
        dynamicDataSource.setTargetDataSources(new HashMap<>());
        return dynamicDataSource;
    }

    @Bean
    @ConditionalOnMissingBean(PlatformTransactionManager.class)
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        log.debug("a default transactionManager that DataSourceTransactionManager will be used  to registe...");
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public BeanPostProcessor mergeDataSourceBeanPostProcessor() {
        return new MergeDataSourceBeanPostProcessor();
    }
}
