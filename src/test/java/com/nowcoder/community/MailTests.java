package com.nowcoder.community;

import com.nowcoder.community.util.MailClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

/**
 * ClassName: MailTests
 * Package: com.nowcoder.community
 * Description:
 *
 * @Autuor Dongjie Sang
 * @Create 2023/5/27 17:11
 * @Version 1.0
 */
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class MailTests {

    @Autowired
    private MailClient mailClient;

    @Autowired
    private TemplateEngine templateEngine;

    @Test
    void testTextMail() {
        mailClient.sendMail("15809286760@163.com", "TEST", "Welcome.");
    }

    @Test
    void testHtmlMail() {
        Context context = new Context();
        context.setVariable("username", "DJWILL");

        String content = templateEngine.process("/mail/demo", context);
        System.out.println(content);

        mailClient.sendMail("15809286760@163.com", "HTML", content);
    }
}
