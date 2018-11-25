package com.example.demo.schedule;

import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

import java.util.concurrent.Executors;

public abstract class MutableScheduler implements SchedulingConfigurer {

    abstract void run();

    abstract String getCron();

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.setScheduler(Executors.newScheduledThreadPool(2));
        // 任务逻辑
        scheduledTaskRegistrar.addTriggerTask(MutableScheduler.this::run, triggerContext -> {
            // 任务触发，可修改任务的执行周期
            return new CronTrigger(getCron()).nextExecutionTime(triggerContext);
        });
    }
}
