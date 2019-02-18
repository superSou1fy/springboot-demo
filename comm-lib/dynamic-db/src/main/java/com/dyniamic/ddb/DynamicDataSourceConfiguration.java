package com.dyniamic.ddb;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.XADataSourceAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

/**
 * 将动态数据源配置到容器
 */
@Slf4j
@Configuration
@AutoConfigureBefore({XADataSourceAutoConfiguration.class, DataSourceAutoConfiguration.class})
@ConditionalOnClass({ DataSource.class, EmbeddedDatabaseType.class })
//@ConditionalOnProperty(prefix = "dynamic", name = "datasource")
@Import(CodedConfiguration.class)
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
}
