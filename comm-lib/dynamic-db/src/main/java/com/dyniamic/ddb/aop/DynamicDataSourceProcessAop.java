package com.dyniamic.ddb.aop;


import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
public class DynamicDataSourceProcessAop {

    @Pointcut("@annotation(com.dyniamic.ddb.aop.DBRouter)")
    public void annoPointCut(){}
}
