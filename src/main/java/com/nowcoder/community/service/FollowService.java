package com.nowcoder.community.service;

import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * ClassName: FollowService
 * Package: com.nowcoder.community.service
 * Description:
 *
 * @Autuor Dongjie Sang
 * @Create 2023/6/3 18:35
 * @Version 1.0
 */
@Service
public class FollowService {

    @Autowired
    private RedisTemplate redisTemplate;


    /**
     * Follow.
     * 关注的业务逻辑
     *
     * @param userId     the user id
     * @param entityType the entity type
     * @param entityId   the entity id
     * @throws InterruptedException the interrupted exception
     */
    public void follow(int userId, int entityType, int entityId) throws InterruptedException {
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);

        String followLock = RedisKeyUtil.getLockByName(followeeKey);
        String uuid = CommunityUtil.generateUUID();
        Boolean store = redisTemplate.opsForValue().setIfAbsent(followLock, uuid, 3, TimeUnit.SECONDS);
        if (store) {

            redisTemplate.opsForZSet().add(followeeKey, entityId, System.currentTimeMillis());
            redisTemplate.opsForZSet().add(followerKey, userId, System.currentTimeMillis());

            String uuidLock = (String) redisTemplate.opsForValue().get(followLock);
            if (uuidLock != null && uuid.equals(uuidLock)) {
                String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
                DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
                redisScript.setScriptText(script);
                redisScript.setResultType(Long.class);
                Long execute = (Long) redisTemplate.execute(redisScript, Arrays.asList(followLock), uuidLock);
                //System.out.println("execute执行结果，1表示执行del操作，0表示未执行 ===== " + execute);
            }
        } else {
            Thread.sleep(100);
            follow(userId, entityType, entityId);
        }
    }

    /**
     * Un follow.
     * 取消关注的业务逻辑
     *
     * @param userId     the user id
     * @param entityType the entity type
     * @param entityId   the entity id
     * @throws InterruptedException the interrupted exception
     */
    public void unFollow(int userId, int entityType, int entityId) throws InterruptedException {
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);

        String followLock = RedisKeyUtil.getLockByName(followeeKey);
        String uuid = CommunityUtil.generateUUID();
        Boolean store = redisTemplate.opsForValue().setIfAbsent(followLock, uuid, 3, TimeUnit.SECONDS);
        if (store) {

            redisTemplate.opsForZSet().remove(followeeKey, entityId);
            redisTemplate.opsForZSet().remove(followerKey, userId);

            String uuidLock = (String) redisTemplate.opsForValue().get(followLock);
            if (uuidLock != null && uuid.equals(uuidLock)) {
                String script = "if redis.call('get', KEYS[1]) == ARGV[1] then return redis.call('del', KEYS[1]) else return 0 end";
                DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
                redisScript.setScriptText(script);
                redisScript.setResultType(Long.class);
                Long execute = (Long) redisTemplate.execute(redisScript, Arrays.asList(followLock), uuidLock);
                //System.out.println("execute执行结果，1表示执行del操作，0表示未执行 ===== " + execute);
            }
        } else {
            Thread.sleep(100);
            follow(userId, entityType, entityId);
        }
    }

    /**
     * Find followee count long.
     * 查询关注的实体的数量
     *
     * @param userId     the user id
     * @param entityType the entity type
     * @return the long
     */
    public long findFolloweeCount(int userId, int entityType) {
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        return redisTemplate.opsForZSet().zCard(followeeKey);
    }

    /**
     * Find follower count long.
     * 查询实体的粉丝的数量
     *
     * @param entityType the entity type
     * @param entityId   the entity id
     * @return the long
     */
    public long findFollowerCount(int entityType, int entityId) {
        String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
        return redisTemplate.opsForZSet().zCard(followerKey);
    }

    /**
     * Has followed boolean.
     * 查询当前用户是否已关注该实体
     *
     * @param userId     the user id
     * @param entityType the entity type
     * @param entityId   the entity id
     * @return the boolean
     */
    public boolean hasFollowed(int userId, int entityType, int entityId) {
        String followeeKey = RedisKeyUtil.getFolloweeKey(userId, entityType);
        return redisTemplate.opsForZSet().score(followeeKey, entityId) != null;
    }
}
