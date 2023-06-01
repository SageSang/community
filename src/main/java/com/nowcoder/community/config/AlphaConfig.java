package com.nowcoder.community.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;

/**
 * ClassName: AlphaConfig
 * Package: com.nowcoder.community.config
 * Description:
 *
 * @Autuor Dongjie Sang
 * @Create 2023/5/25 19:17
 * @Version 1.0
 */
//@Configuration
public class AlphaConfig {
    @Bean
    public SimpleDateFormat simpleDateFormat(){
        return new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
    }
}
