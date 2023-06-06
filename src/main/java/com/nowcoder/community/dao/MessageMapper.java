package com.nowcoder.community.dao;

import com.nowcoder.community.entity.Message;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * ClassName: MessageMapper
 * Package: com.nowcoder.community.dao
 * Description:
 *
 * @Autuor Dongjie Sang
 * @Create 2023/5/31 20:34
 * @Version 1.0
 */
@Mapper
public interface MessageMapper {

    // 查询当前用户的会话列表，针对每个会话只返回一条最新的私信
    List<Message> selectConversations(int userId, int offset, int limit);

    // 查询当前用户的会话数量
    int selectConversationCount(int userId);

    // 查询某个会话所包含的私信列表
    List<Message> selectLetters(String conversationId, int offset, int limit);

    // 查询某个会话所包含的私信数量
    int selectLetterCount(String conversationId);

    // 查询未读私信数量
    int selectLetterUnreadCount(int userId, String conversationId);

    // 新增私信
    int insertMesssage(Message message);

    // 修改消息的状态
    int updateStatus(List<Integer> ids, int status);

    // 查询某一主题下的最新通知
    Message selectLatestNotice(int userId, String topic);

    // 查询某一主题多包涵的通知数量
    int selectNoticeCount(int userId, String topic);

    // 查询未读的通知数量
    int selectNoticeUnreadCount(int userId, String topic);

    // 查询某一主题多包含的全部通知
    List<Message> selectNotices(int userId, String topic, int offset, int limit);

}
