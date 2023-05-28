package com.nowcoder.community.entity;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.util.Date;

/**
 * ClassName: LoginTicket
 * Package: com.nowcoder.community.entity
 * Description:
 *
 * @Autuor Dongjie Sang
 * @Create 2023/5/28 16:11
 * @Version 1.0
 */
@Data
@NoArgsConstructor
public class LoginTicket {
    @Size(min = 0,message = "id不能为负数！")
    private int id;
    @Size(min = 0,message = "id不能为负数！")
    @NotBlank(message = "用户ID不能为空！")
    private int userId;
    @NotBlank(message = "凭证不能为空！")
    private String ticket;
    @Min(value = 0,message = "登陆状态非法！")
    @Max(value = 1,message = "登陆状态非法！")
    @NonNull
    private int status;
    private Date expired;
}
