package com.nowcoder.community.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.security.Key;
import java.util.Map;
import java.util.UUID;

/**
 * ClassName: CommunityUtil
 * Package: com.nowcoder.community.util
 * Description:
 *
 * @Autuor Dongjie Sang
 * @Create 2023/5/27 20:30
 * @Version 1.0
 */
public class CommunityUtil {

    // 生成随机字符串
    public static String generateUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    // MD5加密
    public static String md5(String key) {
        if (StringUtils.isBlank(key)) {
            return null;
        }
        return DigestUtils.md5DigestAsHex(key.getBytes());
    }

    // 处理成json字符串的方法
    public static String getJSONString(int code, String msg, Map<String, Object> map) {
        JSONObject json = new JSONObject();
        json.put("code", code);
        if (msg != null) {
            json.put("msg", msg);
        }
        if (map != null) {
            for (String key : map.keySet()) {
                json.put(key, map.get(key));
            }
        }
        return json.toJSONString();
    }

    public static String getJSONString(int code, String msg) {
        return getJSONString(code, msg, null);
    }

    public static String getJSONString(int code) {
        return getJSONString(code, null, null);
    }


}
