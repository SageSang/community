package com.nowcoder.community.controller;

import com.nowcoder.community.annotation.LoginRequired;
import com.nowcoder.community.entity.Comment;
import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Event;
import com.nowcoder.community.event.EventProducer;
import com.nowcoder.community.service.CommentService;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.util.CommunityConstant;
import com.nowcoder.community.util.HostHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

/**
 * ClassName: CommentController
 * Package: com.nowcoder.community.controller
 * Description:
 *
 * @Autuor Dongjie Sang
 * @Create 2023/5/30 22:44
 * @Version 1.0
 */
@Controller
@RequestMapping("/comment")
public class CommentController implements CommunityConstant {

    @Autowired
    private CommentService commentService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private EventProducer eventProducer;

    @Autowired
    private DiscussPostService discussPostService;

    @PostMapping("/add/{discussPostId}")
    @LoginRequired
    public String addComment(@PathVariable("discussPostId") int discussPostId, Comment comment) {
        comment.setUserId(hostHolder.getUser().getId());
        comment.setStatus(0);
        comment.setCreateTime(new Date());
        int rows = commentService.addComment(comment);
        if (rows == 1) {
            // 触发评论事件
            Event event = new Event()
                    .setTopic(TOPIC_COMMENT)
                    .setUserId(hostHolder.getUser().getId())
                    .setEntityType(comment.getEntityType())
                    .setEntityId(comment.getEntityId())
                    .setData("postId", discussPostId);
            // 根据实体类型的不同，获取不同的作者
            if (comment.getEntityType() == ENTITY_TYPE_POST) {
                DiscussPost target = discussPostService.findDisscussPostById(comment.getEntityId());
                event.setEntityUserId(target.getUserId());
            } else if (comment.getEntityType() == ENTITY_TYPE_COMMENT) {
                Comment target = commentService.findCommentById(comment.getEntityId());
                event.setEntityUserId(target.getUserId());
            }
            // 发布评论事件(Kafka是异步的，程序不会阻塞在这里，会继续向下运行，评论发布在另一个线程中实现)
            eventProducer.fireEvent(event);

            // 返回帖子列表
            return "redirect:/discuss/detail/" + discussPostId;
        } else {
            return "site/error/500";
        }
    }
}
