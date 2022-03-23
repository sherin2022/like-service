package com.example.demo.repo;

import com.example.demo.model.Like;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface LikeRepo extends MongoRepository<Like,String> {
    List<Like> findByPcId(String postOrCommentId);


    Like findByPcIdAndId(String postOrCommentId, String likeId);
}
