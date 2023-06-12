package com.nowcoder.community.service;

import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * ClassName: DiscussPostService
 * Package: com.nowcoder.community.service
 * Description:
 *
 * @Autuor Dongjie Sang
 * @Create 2023 /5/25 22:15
 * @Version 1.0
 */
@Service
public class DiscussPostService {
    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    /**
     * Find discuss posts list.
     *
     * @param userId the user id
     * @param offset the offset
     * @param limit  the limit
     * @return the list
     */
    public List<DiscussPost> findDiscussPosts(int userId, int offset, int limit, int orderMode) {
        return discussPostMapper.selectDiscussPosts(userId, offset, limit, orderMode);
    }

    /**
     * Find discuss post rows int.
     *
     * @param userId the user id
     * @return the int
     */
    public int findDiscussPostRows(int userId) {
        return discussPostMapper.selectDiscussPostRows(userId);
    }

    /**
     * 新增文章的业务逻辑
     *
     * @param post the post
     * @return int
     */
    public int addDisscussPost(DiscussPost post) {
        if (post == null) {
            throw new IllegalArgumentException("参数不能为空！");
        }
        // 转义HTML标记
        post.setTitle(HtmlUtils.htmlEscape(post.getTitle()));
        post.setContent(HtmlUtils.htmlEscape(post.getContent()));
        // 过滤敏感词
        post.setTitle(sensitiveFilter.filter(post.getTitle()));
        post.setContent(sensitiveFilter.filter(post.getContent()));

        return discussPostMapper.insertDiscussPost(post);
    }

    /**
     * 根据帖子id查询帖子
     *
     * @param id the id
     * @return discuss post
     */
    public DiscussPost findDiscussPostById(int id) {
        return discussPostMapper.selectDiscussPostById(id);
    }

    /**
     * 跟据id更新帖子的评论数量
     *
     * @param id           the id
     * @param commentCount the comment count
     * @return int
     */
    public int updateCommentCount(int id, int commentCount) {
        return discussPostMapper.updateCommentCount(id, commentCount);
    }

    /**
     * Update type int.
     * 根据id更新帖子类型
     *
     * @param id   the id
     * @param type the type
     * @return the int
     */
    public int updateType(int id, int type) {
        return discussPostMapper.updateType(id, type);
    }

    /**
     * Update status int.
     * 根据id更新帖子状态
     *
     * @param id     the id
     * @param status the status
     * @return the int
     */
    public int updateStatus(int id, int status) {
        return discussPostMapper.updateStatus(id, status);
    }

    /**
     * Update score int.
     * 根据id更新帖子分数
     *
     * @param id    the id
     * @param score the score
     * @return the int
     */
    public int updateScore(int id, double score) {
        return discussPostMapper.updateScore(id, score);
    }
}
