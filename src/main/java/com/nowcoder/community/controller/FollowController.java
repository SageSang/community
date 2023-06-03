package com.nowcoder.community.controller;

import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.FollowService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * ClassName: FollowController
 * Package: com.nowcoder.community.controller
 * Description:
 *
 * @Autuor Dongjie Sang
 * @Create 2023 /6/3 19:10
 * @Version 1.0
 */
@Controller
public class FollowController implements CommunityConstant {

    @Autowired
    private static final Logger logger = LoggerFactory.getLogger(FollowController.class);

    @Autowired
    private FollowService followService;

    @Autowired
    private UserService userService;

    @Autowired
    private HostHolder hostHolder;

    /**
     * Follow string.
     * 关注的业务逻辑
     *
     * @param entityType the entity type
     * @param entityId   the entity id
     * @return the string
     */
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

    /**
     * Un follow string.
     * 取消关注的业务逻辑
     *
     * @param entityType the entity type
     * @param entityId   the entity id
     * @return the string
     */
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

    /**
     * Gets followees.
     * 获取关注列表的业务逻辑
     *
     * @param userId the user id
     * @param page   the page
     * @param model  the model
     * @return the followees
     */
    @GetMapping("/followees/{userId}")
    public String getFollowees(@PathVariable("userId") int userId, @Validated Page page, Model model) {
        User user = userService.findUserById(userId);
        if (user == null) {
            throw new RuntimeException("该用户不存在！");
        }
        model.addAttribute("user", user);

        page.setLimit(5);
        page.setPath("/followees/" + userId);
        page.setRows((int) followService.findFolloweeCount(userId, ENTITY_TYPE_USER));

        List<Map<String, Object>> userList = followService.findFollowees(userId, page.getOffset(), page.getLimit());
        if (userList != null && !userList.isEmpty()) {
            for (Map<String, Object> map : userList) {
                User u = (User) map.get("user");
                // 获取状态（当前用户是否关注了列表中用户）并存入map
                map.put("hasFollowed", hasFollowed(u.getId()));
            }
        }

        model.addAttribute("users", userList);

        return "site/followee";
    }

    /**
     * Gets followers.
     * 获取粉丝列表的业务逻辑
     *
     * @param userId the user id
     * @param page   the page
     * @param model  the model
     * @return the followers
     */
    @GetMapping("/followers/{userId}")
    public String getFollowers(@PathVariable("userId") int userId, @Validated Page page, Model model) {
        User user = userService.findUserById(userId);
        if (user == null) {
            throw new RuntimeException("该用户不存在！");
        }
        model.addAttribute("user", user);

        page.setLimit(5);
        page.setPath("/followers/" + userId);
        page.setRows((int) followService.findFolloweeCount(ENTITY_TYPE_USER, userId));

        List<Map<String, Object>> userList = followService.findFollowers(userId, page.getOffset(), page.getLimit());
        if (userList != null && !userList.isEmpty()) {
            for (Map<String, Object> map : userList) {
                User u = (User) map.get("user");
                // 获取状态（当前用户是否关注了列表中用户）并存入map
                map.put("hasFollowed", hasFollowed(u.getId()));
            }
        }

        model.addAttribute("users", userList);

        return "site/follower";
    }

    private boolean hasFollowed(int userId) {
        if (hostHolder.getUser() == null) {
            return false;
        }
        return followService.hasFollowed(hostHolder.getUser().getId(), ENTITY_TYPE_USER, userId);
    }
}
