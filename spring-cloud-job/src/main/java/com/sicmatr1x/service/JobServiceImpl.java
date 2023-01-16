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
//        this.addTestJob(TestJob.class, "TestJob", "default", TestJob.getTrigger());
        this.addTestJob(TestCronJob.class, "TestCronJob", "default", TestCronJob.getTrigger());

        // 5. 启动
        stdScheduler.start();
    }

    public void addTestJob(Class<? extends Job> jobClass, String jobName, String jobGroup, Trigger trigger) throws SchedulerException {
        // 2. 任务实例 JobDetail
        // 参数 1：任务的名称；参数 2：任务组的名称
        JobDetail jobDetail = JobBuilder.newJob(jobClass)
                .withIdentity(jobName, jobGroup)
                .build();
        // 3. 触发器 Trigger
        // 4. 通过调度器将触发器与任务实例关联，保证按照触发器定义的条件执行任务
        stdScheduler.scheduleJob(jobDetail, trigger);
    }


}
