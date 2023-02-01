package com.sensenxu.entity;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Event {
    private String topic;
    //触发事件的人
    private int userId;
    //发评论 还是点赞 记录实体类型
    private int entityType;
    //实体
    private int entityId;
    //实体的归属者 比如被点赞的对象
    private int entityUserId;
    private Map<String, Object> data = new HashMap<>();

    public Event setTopic(String topic){
        this.topic = topic;
        return this;
    }
    public Event setUserId(int userId) {
        this.userId = userId;
        return this;
    }
    public Event setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }
    public Event setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }
    public Event setEntityUserId(int entityUserId) {
        this.entityUserId = entityUserId;
        return this;
    }
    public Event setData(String key, Object value) {
        this.data.put(key, value);
        return this;
    }

}
