package com.nowcoder.community.service;

import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * ClassName: DiscussPostService
 * Package: com.nowcoder.community.service
 * Description:
 *
 * @Autuor Dongjie Sang
 * @Create 2023/5/25 22:15
 * @Version 1.0
 */
@Service
public class DiscussPostService {
    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    public List<DiscussPost> findDiscussPosts(int userId, int offset, int limit) {
        return discussPostMapper.selectDiscussPosts(userId, offset, limit);
    }

    public int findDiscussPostRows(int userId) {
        return discussPostMapper.selectDiscussPostRows(userId);
    }

    /**
     * 新增文章的业务逻辑
     *
     * @param post
     * @return
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
}
