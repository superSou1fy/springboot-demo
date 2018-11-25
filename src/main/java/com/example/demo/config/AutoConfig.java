package com.example.demo.config;

import com.example.demo.schedule.ScheduleDelegate;
import com.example.demo.taskdefine.TaskDefinition;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@ConditionalOnBean(value = TaskDefinition.class)
public class AutoConfig {

    @Bean
    public Object registerSchedule(List<TaskDefinition> taskDefinitions, BeanFactory beanFactory) {
        DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) beanFactory;
        for (TaskDefinition taskDefinition : taskDefinitions) {
            defaultListableBeanFactory.registerSingleton(taskDefinition.getTaskName()+"_scheduler", new ScheduleDelegate(taskDefinition));
        }
        return new Object();
    }
}
