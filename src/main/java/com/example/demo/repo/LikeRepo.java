package com.example.demo.repo;

import com.example.demo.model.Like;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeRepo extends MongoRepository<Like,String> {

    public Like save(Like like);
    public Like findByLikeId(String likeId);
}
