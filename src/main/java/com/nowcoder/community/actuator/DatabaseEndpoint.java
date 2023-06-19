package com.nowcoder.community.actuator;

import com.nowcoder.community.util.CommunityUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;
import org.springframework.boot.actuate.endpoint.annotation.ReadOperation;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * ClassName: DatabaseEndpoint
 * Package: com.nowcoder.community.actuator
 * Description:
 *
 * @Autuor Dongjie Sang
 * @Create 2023/6/19 22:25
 * @Version 1.0
 */
@Component
@Endpoint(id="database")
public class DatabaseEndpoint {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseEndpoint.class);

    @Autowired
    private DataSource dataSource;

    /**
     * @ReadOperation代表仅读的Endpoint。即只允许Get请求来访问此端点。
     * 还有@WriteOperation注解，代表Post、Put等请求访问的端点。
     *
     * @return
     */
    @ReadOperation
    private String checkConnection() {
        try (
                Connection conn = dataSource.getConnection();
        ) {
            return CommunityUtil.getJSONString(0, "获取连接成功!");
        } catch (SQLException e) {
            logger.error("获取连接失败: "+e.getMessage());
            return CommunityUtil.getJSONString(1, "获取连接失败!");
        }
    }

}
