package com.nowcoder.community;

import com.nowcoder.community.util.SensitiveFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

/**
 * ClassName: SensitiveTests
 * Package: com.nowcoder.community
 * Description:
 *
 * @Autuor Dongjie Sang
 * @Create 2023/5/29 20:04
 * @Version 1.0
 */
@SpringBootTest
@ContextConfiguration(classes = CommunityApplication.class)
public class SensitiveTests {

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Test
    void testFilter() {
        String text = "这里可以赌博，可以嫖✓娼，可以吸毒，也可以开×票，哈哈哈！";
        String filter = sensitiveFilter.filter(text);
        System.out.println(filter);
    }
}
