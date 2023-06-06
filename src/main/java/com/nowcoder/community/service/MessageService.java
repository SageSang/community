package com.nowcoder.community.service;

import com.nowcoder.community.dao.MessageMapper;
import com.nowcoder.community.entity.Message;
import com.nowcoder.community.util.SensitiveFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * ClassName: MessageService
 * Package: com.nowcoder.community.service
 * Description:
 *
 * @Autuor Dongjie Sang
 * @Create 2023 /5/31 22:17
 * @Version 1.0
 */
@Service
public class MessageService {

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private SensitiveFilter sensitiveFilter;

    /**
     * Find conversations list.
     *
     * @param userId the user id
     * @param offset the offset
     * @param limit  the limit
     * @return the list
     */
    public List<Message> findConversations(int userId, int offset, int limit) {
        return messageMapper.selectConversations(userId, offset, limit);
    }

    /**
     * Find conversation count int.
     *
     * @param userId the user id
     * @return the int
     */
    public int findConversationCount(int userId) {
        return messageMapper.selectConversationCount(userId);
    }

    /**
     * Find letters list.
     *
     * @param conversationId the conversation id
     * @param offset         the offset
     * @param limit          the limit
     * @return the list
     */
    public List<Message> findLetters(String conversationId, int offset, int limit) {
        return messageMapper.selectLetters(conversationId, offset, limit);
    }

    /**
     * Find letter count int.
     *
     * @param conversationId the conversation id
     * @return the int
     */
    public int findLetterCount (String conversationId) {
        return messageMapper.selectLetterCount(conversationId);
    }

    /**
     * Find letter unread count int.
     *
     * @param userId         the user id
     * @param conversationId the conversation id
     * @return the int
     */
    public int findLetterUnreadCount(int userId, String conversationId) {
        return messageMapper.selectLetterUnreadCount(userId, conversationId);
    }

    /**
     * Add message int.
     *
     * @param message the message
     * @return the int
     */
    public int addMessage(Message message) {
        message.setContent(HtmlUtils.htmlEscape(message.getContent()));
        message.setContent(sensitiveFilter.filter(message.getContent()));
        return messageMapper.insertMesssage(message);
    }

    /**
     * 读取消息的业务逻辑（改变消息状态）（未读 --> 已读）
     *
     * @param ids the ids
     * @return the int
     */
    public int readMessage(List<Integer> ids) {
        return messageMapper.updateStatus(ids, 1);
    }

    /**
     * Find latest notice message.
     * 查询最新通知
     *
     * @param userId the user id
     * @param topic  the topic
     * @return the message
     */
    public Message findLatestNotice(int userId, String topic) {
        return messageMapper.selectLatestNotice(userId, topic);
    }

    /**
     * Find notice count int.
     * 查询通知数量
     *
     * @param userId the user id
     * @param topic  the topic
     * @return the int
     */
    public int findNoticeCount(int userId, String topic) {
        return messageMapper.selectNoticeCount(userId, topic);
    }

    /**
     * Fing notice unread count int.
     * 查询未读通知数量
     *
     * @param userId the user id
     * @param topic  the topic
     * @return the int
     */
    public int fingNoticeUnreadCount(int userId,String topic) {
        return messageMapper.selectNoticeUnreadCount(userId, topic);
    }

    /**
     * Find notices list.
     * 分页查询某一主题的全部通知
     *
     * @param userId the user id
     * @param topic  the topic
     * @param offset the offset
     * @param limit  the limit
     * @return the list
     */
    public List<Message> findNotices(int userId, String topic, int offset, int limit) {
        return messageMapper.selectNotices(userId, topic, offset, limit);
    }
}
