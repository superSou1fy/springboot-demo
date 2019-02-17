package com.example.demo.ddb;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

/**
 * 编程式动态数据源操作
 */
public class DynamicDbOperator {
    public static void excute(String key, DbOperateCaller dbOperateCaller) {
        Assert.isTrue(StringUtils.isNotEmpty(key), "数据源标识不能为空");
        DetermineDbKeyContext.routeTo(key);
        try {
            dbOperateCaller.process();
        } finally {
            DetermineDbKeyContext.restoreDbKey();
        }
    }

    @FunctionalInterface
    public interface DbOperateCaller {
        void process();
    }
}
