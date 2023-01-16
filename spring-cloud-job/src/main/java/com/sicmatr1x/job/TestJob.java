package com.sicmatr1x.job;

import org.apache.log4j.Logger;
import org.quartz.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestJob implements Job {

    private static final Logger logger = Logger.getLogger(TestJob.class);

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        // 当前时间
        Date date = new Date();
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = dateFormat.format(date);
        // 工作内容
        logger.info("TestJob execute: " + dateStr);
    }

    /**
     * 创建任务对象并关联触发器
     * @return
     */
    public static Trigger getTrigger() {
        // 参数 1：触发器的名称；参数 2：触发器组的名称
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("TestJobTrigger", "default")
                .withSchedule(SimpleScheduleBuilder.simpleSchedule().repeatSecondlyForever(60))
                .startNow()
                .build();
        return trigger;
    }
}
