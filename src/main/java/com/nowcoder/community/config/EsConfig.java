package com.nowcoder.community.config;

import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.erhlc.RestClients;

/**
 * ClassName: EsConfig
 * Package: com.nowcoder.community.config
 * Description:
 * 实现模糊类 AbstractElasticsearchConfiguration
 * 来得到 RestHighLevelClient 用于查询
 *
 * @Autuor Dongjie Sang
 * @Create 2023/6/7 21:43
 * @Version 1.0
 */
@Configuration
public class EsConfig {
    @Value("${spring.elasticsearch.uris}")
    private String esUrl;

    //localhost:9200 写在配置文件中,直接用 <- spring.elasticsearch.uris
    @Bean
    RestHighLevelClient client() {
        ClientConfiguration clientConfiguration = ClientConfiguration.builder()
                .connectedTo(esUrl)//elasticsearch地址
                .build();

        return RestClients.create(clientConfiguration).rest();
    }
}
