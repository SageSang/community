package com.nowcoder.community.entity;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
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
    @NotBlank(message = "用户名不能为空！")
    @Size(min = 3,max = 15,message = "用户名长度必须在3到15位之间！")
    private String username;
    @NotBlank(message = "密码不能为空！")
    @Size(min = 8,max = 20,message = "密码长度必须在8到20位之间！")
    private String password;
    private String salt;
    @NotBlank(message = "邮箱不能为空！")
    @Email(message = "请注意邮箱格式！")
    private String email;
    private int type;
    private int status;
    private String activationCode;
    private String headerUrl;
    private Date createTime;

}
