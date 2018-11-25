package com.example.demo.config;

import org.springframework.context.ApplicationContext;

public class ApplicationUtils{

    private static ApplicationContext applicationContext;

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static void setApplicationContext(ApplicationContext applicationContext) {
        ApplicationUtils.applicationContext = applicationContext;
    }
}
