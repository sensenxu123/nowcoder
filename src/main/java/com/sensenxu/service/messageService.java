package com.sensenxu.service;

import com.sensenxu.entity.Message;
import org.springframework.stereotype.Service;

import java.util.List;

public interface messageService {
    List<Message> findConversations(int userId, int offset, int limit);
    int findConversationCount(int userId);
    List<Message> findLetters(String conversationId, int offset, int limit);
    int findLetterCount(String conversationId);
    int findLetterUnreadCount(int userId, String conversationId);
    int addMessage(Message message);
    //回复读了，更新状态为已读
    int readMessage(List<Integer> ids);
}
