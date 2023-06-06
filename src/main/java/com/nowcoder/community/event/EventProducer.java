package com.nowcoder.community.event;

import com.alibaba.fastjson.JSONObject;
import com.nowcoder.community.entity.Event;
import com.nowcoder.community.util.CommunityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * ClassName: EventProducer
 * Package: com.nowcoder.community.event
 * Description:
 *
 * @Autuor Dongjie Sang
 * @Create 2023/6/6 18:30
 * @Version 1.0
 */
@Component
public class EventProducer {

    @Autowired
    private KafkaTemplate kafkaTemplate;


    /**
     * Fire event.
     * 处理事件
     *
     * @param event the event
     */
    public void fireEvent(Event event) {
        // 将事件发布到指定的主题
        kafkaTemplate.send(event.getTopic(), JSONObject.toJSONString(event));
    }

}
