package com.sensenxu.service;

import com.sensenxu.entity.discussPost;
import org.springframework.data.domain.Page;

public interface elasticSearchService {
    public void saveDiscussPost(discussPost post);
    public void deleteDiscussPost(int id);
    public Page<discussPost> searchDiscussPost(String keyword, int current, int limit);

}
