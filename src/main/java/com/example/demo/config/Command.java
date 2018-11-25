package com.example.demo.config;

import com.example.demo.schedule.ScheduleDelegate;
import com.example.demo.taskdefine.TaskDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.CommandLineRunner;

import java.util.Map;

import static org.springframework.beans.factory.config.BeanDefinition.SCOPE_SINGLETON;
import static org.springframework.beans.factory.support.AbstractBeanDefinition.AUTOWIRE_BY_NAME;

public class Command implements CommandLineRunner {

    @Override
    public void run(String... args) throws Exception {
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) ApplicationUtils.getApplicationContext().getAutowireCapableBeanFactory();
        Map<String, TaskDefinition> taskDefinitionMap = defaultListableBeanFactory.getBeansOfType(TaskDefinition.class);
        for (Map.Entry<String, TaskDefinition> entry : taskDefinitionMap.entrySet()) {
            String beanName = entry.getKey();
            TaskDefinition definition = entry.getValue();
            defaultListableBeanFactory.registerBeanDefinition(definition.getTaskName() + "_scheduler", getScheduleBeanDefinitions(beanName, definition));
        }
    }

    private AbstractBeanDefinition getScheduleBeanDefinitions(String beanName, TaskDefinition taskDefinition) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(ScheduleDelegate.class)
                .addConstructorArgValue(taskDefinition).setScope(SCOPE_SINGLETON);
        return builder.getBeanDefinition();
    }
}
