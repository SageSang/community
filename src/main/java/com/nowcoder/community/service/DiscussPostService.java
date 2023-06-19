package com.nowcoder.community.service;

import com.github.benmanes.caffeine.cache.CacheLoader;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
import com.nowcoder.community.dao.DiscussPostMapper;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.util.SensitiveFilter;
import jakarta.annotation.PostConstruct;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;
import java.util.concurrent.TimeUnit;

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

    private static final Logger logger = LoggerFactory.getLogger(DiscussPostService.class);

    @Autowired
    private DiscussPostMapper discussPostMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    @Value("${caffeine.posts.max-size}")
    private int maxSize;

    @Value("${caffeine.posts.expire-seconds}")
    private int expireSeconds;

    /**
     * Caffeine核心接口：Cache
     * 常用的两个子接口：LoadingCache, AsyncLoadingCache
     * LoadingCache是同步缓存，取的时候需要等待；AsyncLoadingCache是异步缓存，支持并发。
     */
    // 帖子列表的缓存
    private LoadingCache<String, List<DiscussPost>> postListCache;

    // 帖子总数的缓存
    private LoadingCache<Integer, Integer> postRowsCache;

    @PostConstruct
    public void init() {
        // 初始化帖子列表缓存
        postListCache = Caffeine.newBuilder()
                .maximumSize(maxSize)
                .expireAfterWrite(expireSeconds, TimeUnit.SECONDS)
                .build(new CacheLoader<String, List<DiscussPost>>() {
                    @Override
                    public @Nullable List<DiscussPost> load(String key) throws Exception {
                        if (key == null || key.length() == 0) {
                            throw new IllegalArgumentException("参数错误！");
                        }
                        String[] params = key.split(":");
                        if (params == null || params.length != 2) {
                            throw new IllegalArgumentException("参数错误！");
                        }
                        int offset = Integer.valueOf(params[0]);
                        int limit = Integer.valueOf(params[1]);

                        // 这里可以加二级缓存：Redis -> mysql
                        // 等后续再增加二级Redis缓存，暂时先不实现。

                        logger.debug("load post list from DB.");
                        return discussPostMapper.selectDiscussPosts(0, offset, limit, 1);
                    }
                });
        // 初始化帖子总数缓存
        postRowsCache = Caffeine.newBuilder()
                .maximumSize(maxSize)
                .expireAfterWrite(expireSeconds, TimeUnit.SECONDS)
                .build(new CacheLoader<Integer, Integer>() {
                    @Override
                    public @Nullable Integer load(Integer key) throws Exception {
                        if (key == null) {
                            throw new IllegalArgumentException("参数错误！");
                        }

                        // 这里可以加二级缓存：Redis -> mysql
                        // 等后续再增加二级Redis缓存，暂时先不实现。

                        logger.debug("load post rows from DB.");
                        return discussPostMapper.selectDiscussPostRows(key);
                    }
                });
    }

    /**
     * Find discuss posts list.
     *
     * @param userId the user id
     * @param offset the offset
     * @param limit  the limit
     * @return the list
     */
    public List<DiscussPost> findDiscussPosts(int userId, int offset, int limit, int orderMode) {
        // 读取本地缓存
        if (userId == 0 && orderMode == 1) {
            return postListCache.get(offset + ":" + limit);
        }
        // 访问数据库
        logger.debug("load post list from DB.");
        return discussPostMapper.selectDiscussPosts(userId, offset, limit, orderMode);
    }

    /**
     * Find discuss post rows int.
     *
     * @param userId the user id
     * @return the int
     */
    public int findDiscussPostRows(int userId) {
        // 读取本地缓存
        if (userId == 0) {
            return postRowsCache.get(userId);
        }
        // 访问数据库
        logger.debug("load post rows from DB.");
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
