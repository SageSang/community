package com.nowcoder.community.service;

import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.RedisKeyUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * ClassName: LikeService
 * Package: com.nowcoder.community.service
 * Description:
 *
 * @Autuor Dongjie Sang
 * @Create 2023 /6/2 21:03
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
     * @param userId       the user id 点赞的用户id
     * @param entityType   the entity type 点赞实体的类型
     * @param entityId     the entity id   点赞实体的id
     * @param entityUserId 被点赞帖子作者用户的id
     * @throws InterruptedException the interrupted exception
     */
    public void like(int userId, int entityType, int entityId, int entityUserId) throws InterruptedException {
        // 获取要进行点赞操作的key
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        // 获取被点赞用户的key
        String userLikeKey = RedisKeyUtil.getUserLikeKey(entityUserId);

        // 获取分布式锁key(使用被点赞帖子的key生成锁key，用此锁保证点赞操作的原子性)
        String LikeLock = RedisKeyUtil.getLockByName(entityLikeKey);
        // 获取随机字符串(锁的val)
        String uuid = CommunityUtil.generateUUID();
        // 创建分布式锁
        Boolean store = redisTemplate.opsForValue().setIfAbsent(LikeLock, uuid, 3, TimeUnit.SECONDS);
        if (store) { // 拿到锁，执行业务

            // 判断当前用户是否对实体点赞，根据不同情况执行业务
            Boolean isMember = redisTemplate.opsForSet().isMember(entityLikeKey, userId);
            if (isMember) {
                redisTemplate.opsForSet().remove(entityLikeKey, userId);
                redisTemplate.opsForValue().decrement(userLikeKey);
            } else {
                redisTemplate.opsForSet().add(entityLikeKey, userId);
                redisTemplate.opsForValue().increment(userLikeKey);
            }

            // 执行业务完毕，删除锁
            String uuidLock = (String) redisTemplate.opsForValue().get(LikeLock);
            if (uuidLock != null && uuid.equals(uuidLock)) {
                // 定义lua 脚本
                // -- lua删除锁：
                // -- KEYS和ARGV分别是以集合方式传入的参数，对应上文的LikeLock和uuid。
                // -- 如果对应的value等于传入的uuid。
                String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
                // 使用redis执行lua执行
                DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
                redisScript.setScriptText(script);
                // 设置一下返回值类型 为Long
                // 因为删除判断的时候，返回的0,给其封装为数据类型。如果不封装那么默认返回String 类型，
                // 那么返回字符串与0 会有发生错误。
                redisScript.setResultType(Long.class);
                // 第一个要是script 脚本 ，第二个需要判断的key(KEYS[1])，第三个就是key所对应的值(ARGV[1])。
                Long execute = (Long) redisTemplate.execute(redisScript, Arrays.asList(LikeLock), uuidLock);
                //System.out.println("execute执行结果，1表示执行del操作，0表示未执行 ===== " + execute);
            }

        } else {
            // 没有拿到锁，间隔一点时间（不要一直循环）重试
            Thread.sleep(100);
            like(userId, entityType, entityId, entityUserId);
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
    public int findEntityLikeStatus(int userId, int entityType, int entityId) {
        String entityLikeKey = RedisKeyUtil.getEntityLikeKey(entityType, entityId);
        return redisTemplate.opsForSet().isMember(entityLikeKey, userId) ? 1 : 0;
    }


    /**
     * Find user like count int.
     * 查询某个用户获得的赞的数量
     *
     * @param userId the user id
     * @return the int
     */
    public int findUserLikeCount(int userId) {
        String userLikeKey = RedisKeyUtil.getUserLikeKey(userId);
        Integer count = (Integer) redisTemplate.opsForValue().get(userLikeKey);
        return count == null ? 0 : count;
    }

}
