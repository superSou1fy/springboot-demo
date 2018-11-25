package com.example.demo.schedule;

import com.example.demo.taskdefine.TaskDefinition;

public class ScheduleDelegate extends MutableScheduler {

    private TaskDefinition task;

    public ScheduleDelegate(TaskDefinition task) {
        this.task = task;
    }

    @Override
    void run() {
        task.process();
    }

    @Override
    String getCron() {
        return task.getCron();
    }
}
