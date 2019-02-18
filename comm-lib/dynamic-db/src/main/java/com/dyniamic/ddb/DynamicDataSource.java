package com.dyniamic.ddb;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.lang.reflect.Field;
import java.util.Map;

/**
 * 动态数据源
 */
public class DynamicDataSource extends AbstractRoutingDataSource {

    @Override
    protected Object determineCurrentLookupKey() {
        return DetermineDbKeyContext.currentDbKey();
    }

    /**
     * expose  targetDataSources
     *
     * @return
     */
    public Map<Object, Object> getTargetDataSources() {
        try {
            Field targetDataSources = AbstractRoutingDataSource.class.getDeclaredField("targetDataSources");
            targetDataSources.setAccessible(true);
            Object object = targetDataSources.get(this);
            return object == null ? null : Map.class.cast(object);
        } catch (Exception e) {
            throw new RuntimeException("未能正确获取targetDataSources", e);
        }
    }
}
