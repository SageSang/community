package com.nowcoder.community;

import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.dao.LoginTicketMapper;
import com.nowcoder.community.dao.MessageMapper;
import com.nowcoder.community.dao.UserMapper;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.LoginTicket;
import com.nowcoder.community.entity.Message;
import com.nowcoder.community.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

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
        List<DiscussPost> list = discussPostMapper.selectDiscussPosts(149, 0, 10,0);
        list.forEach(System.out::println);

        int rows = discussPostMapper.selectDiscussPostRows(149);
        System.out.println("rows = " + rows);
    }

    @Autowired
    private LoginTicketMapper loginTicketMapper;

    @Test
    void testInsertLoginTicket() {
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(101);
        loginTicket.setTicket("abc");
        loginTicket.setStatus(0);
        loginTicket.setExpired(new Date(System.currentTimeMillis() + 1000 * 60 * 10));

        int rows = loginTicketMapper.insertLoginTicket(loginTicket);
        System.out.println("rows = " + rows);
    }

    @Test
    void testSelectLoginTicket() {
        LoginTicket loginTicket = loginTicketMapper.selectByTicket("abc");
        System.out.println("修改前loginTicket = " + loginTicket);

        loginTicketMapper.updateStatus("abc", 1);
        loginTicket = loginTicketMapper.selectByTicket("abc");
        System.out.println("修改后loginTicket = " + loginTicket);
    }

    @Autowired
    private MessageMapper messageMapper;

    @Test
    void testSelectLetters() {
        List<Message> list = messageMapper.selectConversations(111, 0, 20);
        for (Message message : list) {
            System.out.println(message);
        }

        int count = messageMapper.selectConversationCount(111);
        System.out.println("count = " + count);

        list = messageMapper.selectLetters("111_112", 0, 10);
        for (Message message : list) {
            System.out.println(message);
        }

        count = messageMapper.selectLetterCount("111_112");
        System.out.println("count = " + count);

        count = messageMapper.selectLetterUnreadCount(131, "111_131");
        System.out.println("count = " + count);
    }

}
