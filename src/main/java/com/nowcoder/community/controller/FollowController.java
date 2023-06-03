package com.nowcoder.community.controller;

import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.FollowService;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * ClassName: FollowController
 * Package: com.nowcoder.community.controller
 * Description:
 *
 * @Autuor Dongjie Sang
 * @Create 2023/6/3 19:10
 * @Version 1.0
 */
@Controller
public class FollowController {

    @Autowired
    private static final Logger logger = LoggerFactory.getLogger(FollowController.class);

    @Autowired
    private FollowService followService;

    @Autowired
    private HostHolder hostHolder;

    @PostMapping("/follow")
    @ResponseBody
    @LoginRequired
    public String follow(int entityType, int entityId) {
        User user = hostHolder.getUser();

        // 关注
        try {
            followService.follow(user.getId(), entityType, entityId);
        } catch (InterruptedException e) {
            logger.error("关注失败：" + e.getMessage());
        }

        return CommunityUtil.getJSONString(0, "已关注！");
    }

    @PostMapping("/unfollow")
    @ResponseBody
    @LoginRequired
    public String unFollow(int entityType, int entityId) {
        User user = hostHolder.getUser();

        // 关注
        try {
            followService.unFollow(user.getId(), entityType, entityId);
        } catch (InterruptedException e) {
            logger.error("关注失败：" + e.getMessage());
        }

        return CommunityUtil.getJSONString(0, "已取消关注！");
    }

}
