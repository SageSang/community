package com.nowcoder.community;

import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

/**
 * ClassName: MapperTests
 * Package: com.nowcoder.community
 * Description:
 *
 * @Autuor Dongjie Sang
 * @Create 2023/5/25 21:09
 * @Version 1.0
 */

@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MapperTests {
    @Autowired
    private UserMapper userMapper;

    @Test
    void testSelectUser() {
        User user = userMapper.selectById(101);
        System.out.println("user = " + user);
        user = userMapper.selectByName("liubei");
        System.out.println("user = " + user);
        user = userMapper.selectByEmail("nowcoder101@sina.com");
        System.out.println("user = " + user);
    }

    @Test
    void testInsertUser() {
        User user = new User();
        user.setUsername("test");
        user.setPassword("123456");
        user.setSalt("abc");
        user.setEmail("test@qq.com");
        user.setHeaderUrl("http://www.nowcoder.com/101.png");
        user.setCreateTime(new Date());
        int rows = userMapper.insertUser(user);
        System.out.println("rows = " + rows);
        System.out.println(user.getId());
    }

    @Test
    void updateUser() {
        int rows = userMapper.updateStatus(150, 1);
        System.out.println("rows = " + rows);
        rows = userMapper.updateHeader(150, "http://www.nowcoder.com/102.png");
        System.out.println("rows = " + rows);
        rows = userMapper.updatePassword(150, "hello");
        System.out.println("rows = " + rows);
    }

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Test
    void testSelectPosts() {
        List<DiscussPost> list = discussPostMapper.selectDiscussPosts(149, 0, 10);
        list.forEach(System.out::println);

        int rows = discussPostMapper.selectDiscussPostRows(149);
        System.out.println("rows = " + rows);
    }
}
