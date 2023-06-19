package com.nowcoder.community;

import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.service.DiscussPostService;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.event.annotation.AfterTestClass;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import java.util.Date;

/**
 * ClassName: SpringBootTests
 * Package: com.nowcoder.community
 * Description:
 *
 * @Autuor Dongjie Sang
 * @Create 2023/6/19 20:42
 * @Version 1.0
 */
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class SpringBootTests {
    @Autowired
    private DiscussPostService discussPostService;

    private DiscussPost data;

    @BeforeAll
    public static void beforeAll() {
        System.out.println("beforeAll");
    }

    @AfterAll
    public static void afterAll() {
        System.out.println("afterAll");
    }

    @BeforeEach
    public void beforeEach() {
        System.out.println("beforeEach");

        // 初始化测试数据
        data = new DiscussPost();
        data.setUserId(111);
        data.setTitle("Test Title");
        data.setContent("Test Content");
        data.setCreateTime(new Date());
        discussPostService.addDisscussPost(data);
    }

    @AfterEach
    public void afterEach() {
        System.out.println("afterEach");

        // 删除测试数据
        discussPostService.updateStatus(data.getId(), 2);
    }

    @Test
    void test1() {
        System.out.println("test1");
    }

    @Test
    void test2() {
        System.out.println("test2");
    }

    @Test
    void testFindById() {
        DiscussPost post = discussPostService.findDiscussPostById(data.getId());
        Assertions.assertNotNull(post);
        Assertions.assertEquals(data.getTitle(), post.getTitle());
        Assertions.assertEquals(data.getContent(), post.getContent());
        Assertions.assertEquals(data.getId(), post.getId());
    }

    @Test
    void testUpdateScore() {
        int rows = discussPostService.updateScore(data.getId(), 2000);
        Assertions.assertEquals(1, rows);
        DiscussPost post = discussPostService.findDiscussPostById(data.getId());
        Assertions.assertEquals(2000.00, post.getScore(), 2);
    }
}
