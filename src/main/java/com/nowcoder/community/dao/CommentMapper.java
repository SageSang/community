package com.nowcoder.community.dao;

import com.nowcoder.community.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * ClassName: CommentMapper
 * Package: com.nowcoder.community.dao
 * Description:
 *
 * @Autuor Dongjie Sang
 * @Create 2023/5/29 23:42
 * @Version 1.0
 */
@Mapper
public interface CommentMapper {

    List<Comment> selectCommentsByEntity(int entityType, int entityId, int offset, int limit);

    int selectCountByEntity(int entityType, int entityId);

}
