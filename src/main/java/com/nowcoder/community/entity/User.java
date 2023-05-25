package com.nowcoder.community.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * ClassName: User
 * Package: com.nowcoder.community.entity
 * Description:
 *
 * @Autuor Dongjie Sang
 * @Create 2023/5/25 20:37
 * @Version 1.0
 */
@Data
public class User {

    private int id;
    private String username;
    private String password;
    private String salt;
    private String email;
    private int type;
    private int status;
    private String activationCode;
    private String headerUrl;
    private Date createTime;

}
