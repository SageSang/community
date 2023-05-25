package com.nowcoder.community.entity;

import lombok.Data;

import java.util.Date;

/**
 * ClassName: DiscussPost
 * Package: com.nowcoder.community.entity
 * Description:
 *
 * @Autuor Dongjie Sang
 * @Create 2023/5/25 21:54
 * @Version 1.0
 */
@Data
public class DiscussPost {
    private int id;
    private int userId;
    private String title;
    private String content;
    private int type;
    private int status;
    private Date createTime;
    private int commentCount;
    private double score;
}
