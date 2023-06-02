package com.nowcoder.community.service;

import com.nowcoder.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * ClassName: LikeService
 * Package: com.nowcoder.community.service
 * Description:
 *
 * @Autuor Dongjie Sang
 * @Create 2023/6/2 21:03
 * @Version 1.0
 */
@Service
public class LikeService {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * Like.
     * 点赞的业务逻辑
     *
     * @param userId     the user id
     * @param entityType the entity type
     * @param entityId   the entity id
     */
    public void like(int userId, int entityType,int entityId) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        Boolean isMember = redisTemplate.opsForSet().isMember(entityLikeKey, userId);
        if (isMember) {
            redisTemplate.opsForSet().remove(entityLikeKey, userId);
        } else {
            redisTemplate.opsForSet().add(entityLikeKey, userId);
        }

    }

    /**
     * Find entity like count long.
     * 查询某个实体点赞的数量
     *
     * @param entityType the entity type
     * @param entityId   the entity id
     * @return the long
     */
    public long findEntityLikeCount(int entityType, int entityId) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().size(entityLikeKey);
    }

    /**
     * Find entity like status int.
     * 查询某人对某实体的点赞状态
     *
     * @param userId     the user id
     * @param entityType the entity type
     * @param entityId   the entity id
     * @return the int
     */
    public int findEntityLikeStatus(int userId,int entityType, int entityId) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().isMember(entityLikeKey, userId) ? 1 : 0;
    }

}
