package com.example.demo.taskdefine;

public interface TaskDefinition<T> {
    String getTaskName();

    String getCron();


    void process( );

}
