package com.example.demo.taskdefine;

import com.example.demo.mapper.SaRegionInfoMapper;
import com.example.demo.model.SaRegionInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.annotation.Schedules;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Component
@Slf4j
public class Task02 implements TaskDefinition<String> {


    @Override
    public String getTaskName() {
        return "Task02";
    }

    @Override
    public String getCron() {
        return "*/3 * * * * ?";
    }


    @Override
    public void process() {
        log.info("process2222222 when time:【{}】", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Scheduled(cron = "*/5 * * * * ?")
    public void doTask02() {
        log.info("doTask02 when time:【{}】", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
    }

}
