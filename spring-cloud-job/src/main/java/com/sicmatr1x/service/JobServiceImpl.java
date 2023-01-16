package com.sicmatr1x.service;

import com.sicmatr1x.job.TestCronJob;
import com.sicmatr1x.job.TestJob;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class JobServiceImpl implements JobService {
    Scheduler stdScheduler = StdSchedulerFactory.getDefaultScheduler();
    List<Job> jobList = new ArrayList<>();

    public JobServiceImpl() throws SchedulerException {
        if (this.stdScheduler == null) {
            this.stdScheduler = StdSchedulerFactory.getDefaultScheduler();
        }
        // 2-4. 创建任务对象并关联触发器
//        this.addJob(TestJob.class, "TestJob", "default", TestJob.getTrigger());
//        this.addJob(TestCronJob.class, "TestCronJob", "default", TestCronJob.getTrigger());
        this.addCronJob(TestCronJob.class, "TestCronJob", "default", "TestCron", "0/10 * * * * ? ");

        // 5. 启动
        stdScheduler.start();
    }

    /**
     * 添加job
     * @param jobClass 新添加的job的java类
     * @param jobName job名
     * @param jobGroup job分组
     * @param trigger job调起的触发器
     * @throws SchedulerException
     */
    public void addJob(Class<? extends Job> jobClass, String jobName, String jobGroup, Trigger trigger) throws SchedulerException {
        // 2. 任务实例 JobDetail
        // 参数 1：任务的名称；参数 2：任务组的名称
        JobDetail jobDetail = JobBuilder.newJob(jobClass)
                .withIdentity(jobName, jobGroup)
                .build();
        // 3. 触发器 Trigger
        // 4. 通过调度器将触发器与任务实例关联，保证按照触发器定义的条件执行任务
        stdScheduler.scheduleJob(jobDetail, trigger);
    }

    /**
     * 添加cron job
     * 会在 cronStr 时调起执行
     * @param jobClass 新添加的job的java类
     * @param jobName job名
     * @param jobGroup job分组
     * @param triggerName 触发器名
     * @param cronStr cron时间格式
     * @throws SchedulerException
     */
    public void addCronJob(Class<? extends Job> jobClass, String jobName, String jobGroup, String triggerName, String cronStr) throws SchedulerException {
        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity(triggerName, "default")
                .withSchedule(CronScheduleBuilder.cronSchedule(cronStr))
                .startNow()
                .build();
        this.addJob(jobClass, jobName, jobGroup, trigger);
    }

}
