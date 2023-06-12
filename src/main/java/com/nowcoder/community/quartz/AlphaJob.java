package com.nowcoder.community.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * ClassName: AlphaJob
 * Package: com.nowcoder.community.quartz
 * Description:
 *
 * @Autuor Dongjie Sang
 * @Create 2023/6/12 17:12
 * @Version 1.0
 */
public class AlphaJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        System.out.println(Thread.currentThread().getName() + ": execute a quartz job.");
    }
}
