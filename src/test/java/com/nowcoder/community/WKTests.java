package com.nowcoder.community;

import java.io.IOException;

/**
 * ClassName: WKTests
 * Package: com.nowcoder.community
 * Description:
 *
 * @Autuor Dongjie Sang
 * @Create 2023/6/17 23:07
 * @Version 1.0
 */
public class WKTests {
    public static void main(String[] args) {
        String cmd = """
                E:/Java/Configuration/wkhtmltopdf/wkhtmltox/bin/wkhtmltoimage --quality 75 https://www.nowcoder.com D:/新建文件夹/wk-imgs/2.png            
                """;
        try {
            Runtime.getRuntime().exec(cmd);
            System.out.println("ok.");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
