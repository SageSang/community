package com.nowcoder.community.controller;

import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * ClassName: DiscussPostController
 * Package: com.nowcoder.community.controller
 * Description:
 *
 * @Autuor Dongjie Sang
 * @Create 2023/5/29 21:05
 * @Version 1.0
 */
@Controller
@RequestMapping("/discuss")
public class DiscussPostController {
    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private HostHolder hostHolder;

    /**
     * 新增文章的业务
     *
     * @param title
     * @param content
     * @return
     */
    @PostMapping("/add")
    @ResponseBody
    public String addDiscussPost(@RequestParam String title, @RequestParam String content) {
        User user = hostHolder.getUser();
        if (user == null) {
            return CommunityUtil.getJSONString(403, "你还没有登陆哦！");
        }
        DiscussPost post = new DiscussPost();
        post.setUserId(user.getId());
        post.setTitle(title);
        post.setContent(content);
        post.setCreateTime(new Date());
        discussPostService.addDisscussPost(post);

        // 报错的情况，将来统一处理
        return CommunityUtil.getJSONString(0, "发布成功！");
    }
}
