package com.nowcoder.community;

import com.nowcoder.community.service.AlphaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

/**
 * ClassName: TransactionTests
 * Package: com.nowcoder.community
 * Description:
 *
 * @Autuor Dongjie Sang
 * @Create 2023/5/29 23:20
 * @Version 1.0
 */
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class TransactionTests {
    @Autowired
    private AlphaService alphaService;

    @Test
    void testSave1() {
        Object obj = alphaService.save1();
        System.out.println("obj = " + obj);
    }

    @Test
    void testSave2() {
        Object obj = alphaService.save2();
        System.out.println("obj = " + obj);
    }
}
