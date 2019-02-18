package com.dyniamic.ddb.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DBRouter {
    /**
     * 显示指定数据源路由值
     *
     * @return
     */
    String explicitDbKey() default "";

    /**
     * 方法参数内路由值的搜寻字段名
     *
     * @return
     */
    String seachField() default "";

    /**
     * 路由值所处在的方法参数位置
     *
     * @return
     */
    int argIndex() default 0;
}
