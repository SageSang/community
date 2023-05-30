package com.nowcoder.community.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

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
    private Integer entityType;
    private Integer entityId;
    private Integer targetId;
    private String content;
    private Integer status;
    private Date createTime;

}
