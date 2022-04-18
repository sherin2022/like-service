package com.example.demo.repo;

import com.example.demo.model.Like;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface LikeRepo extends MongoRepository<Like,String> {

    public Like save(Like like);
    public Optional<Like> findById(String likeId);
    List<Like> findByPcId(String postOrCommentId, Pageable page);

    List<Like> findByPcId(String postOrCommentId);

    Like findByPcIdAndId(String postOrCommentId, String likeId);
}
