package com.dyniamic.demo.schedule;

import com.dyniamic.ddb.DynamicDbOperator;
import com.dyniamic.test.TestBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduleBiz {

    @Autowired(required = false)
    private TestBean testBean;

    @Scheduled(cron = "*/20 * * * * *")
    public void doSomeThings() {
        System.out.println("123456/...");
        testBean.sayHi();
        DynamicDbOperator.excute("200", ScheduleBiz::process);
    }

    private static void process() {
        System.out.println("do bizzzzzzzzzz!");
    }
}
