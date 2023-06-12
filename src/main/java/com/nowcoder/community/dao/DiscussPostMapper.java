package com.nowcoder.community.dao;

import com.nowcoder.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * ClassName: DiscussPostMapper
 * Package: com.nowcoder.community.dao
 * Description:
 *
 * @Autuor Dongjie Sang
 * @Create 2023/5/25 21:56
 * @Version 1.0
 */
@Mapper
public interface DiscussPostMapper {

    // 重构了，可以支持热帖排行
    //List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit);

    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit, int orderMode);

    // @Param注解用于给参数取别名
    // 如果只有一个参数，并且在<if>里使用，则必须加别名
    int selectDiscussPostRows(@Param("userId") int userId);

    // 新增文章
    int insertDiscussPost(DiscussPost discussPost);

    // 查询帖子的详情
    DiscussPost selectDiscussPostById(int id);

    // 根据id更新帖子的评论数量
    int updateCommentCount(int id, int commentCount);

    // 根据id更新帖子类型
    int updateType(int id, int type);

    // 根据id更新帖子状态
    int updateStatus(int id, int status);

    // 根据id更新帖子分数
    int updateScore(int id, double score);
}
