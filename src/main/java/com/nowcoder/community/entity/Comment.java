package com.nowcoder.community.entity;

import jakarta.mail.MessagingException;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NonNull;
import org.apache.logging.log4j.message.Message;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

/**
 * ClassName: Comment
 * Package: com.nowcoder.community.entity
 * Description:
 *
 * @Autuor Dongjie Sang
 * @Create 2023/5/29 23:32
 * @Version 1.0
 */
@Data
public class Comment {

    private int id;
    private Integer userId;
    @NotNull(message = "必须存在评论对象")
    @Min(value = 1, message = "评论对象非法")
    private Integer entityType;
    @NotNull(message = "必须存在评论Id")
    @Min(value = 1, message = "评论Id非法")
    private Integer entityId;
    private Integer targetId;
    @NotBlank(message = "必须存在评论内容！")
    private String content;
    private Integer status;
    private Date createTime;

}
