package com.nowcoder.community;

import org.junit.jupiter.api.Test;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.quartz.Scheduler;
import org.springframework.test.context.ContextConfiguration;

/**
 * ClassName: QuartzTests
 * Package: com.nowcoder.community
 * Description:
 *
 * @Autuor Dongjie Sang
 * @Create 2023/6/12 18:01
 * @Version 1.0
 */
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class QuartzTests {

    @Autowired
    private Scheduler scheduler;

    @Test
    void testDeleteJob() {
        try {
            boolean result = scheduler.deleteJob(new JobKey("alphaJob", "alphaJobGroup"));
            System.out.println("result = " + result);
        } catch (SchedulerException e) {
            e.printStackTrace();
        }
    }
}
