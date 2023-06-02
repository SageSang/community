package com.nowcoder.community.util;

/**
 * ClassName: RedisKeyUtil
 * Package: com.nowcoder.community.util
 * Description:
 *
 * @Autuor Dongjie Sang
 * @Create 2023/6/2 20:46
 * @Version 1.0
 */
public class RedisKeyUtil {

    private static final String SPLIT = ":";
    private static final String PREFIX_ENTITY_LIKE = "like:entity";

    // 生成某个实体的赞
    // like:entity:entityType:entityId -> set(userId)
    public static String getEntityLikeKey(int entityType, int entityId) {
        return PREFIX_ENTITY_LIKE + SPLIT + entityType + SPLIT + entityId;
    }

}
