package com.example.demo.service;

import com.example.demo.dto.LikeDto;
import com.example.demo.dto.LikeRequest;
import com.example.demo.feign.UserFeign;
import com.example.demo.model.Like;
import com.example.demo.repo.LikeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public class LikeServiceImpl implements LikeService {
    @Autowired
    LikeRepo likeRepo;

    @Autowired
    UserFeign userFeign;

    @Autowired
    MongoTemplate mongoTemplate;


    @Override
    public List<Like> getLikes(String postOrCommentId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("postOrCommentId").is(postOrCommentId));
        List<Like> listOfLikes = mongoTemplate.find(query, Like.class);
        return listOfLikes;
    }

    @Override
    public Like createLike(String postOrCommentId, LikeRequest likeRequest) {
        Like like = new Like();
        like.setPostOrCommentId(postOrCommentId);
        like.setLikedBy(likeRequest.getLikedBy());
        like.setCreatedAt(new Date());
        return likeRepo.save(like);
    }

    @Override
    public LikeDto getLikeDetails(String likeId) {
        Like like = likeRepo.findByLikeId(likeId);
        String userName = userFeign.getUserById(like.getLikedBy()).getBody().getFirstName();
        LikeDto likeDto = new LikeDto(like.getLikeId(),like.getPostOrCommentId(),like.getLikedBy(),like.getCreatedAt(),userName);
        return likeDto;
    }
    @Override
    public Like removeLike(String likeId) {

        //Like likeToBeDeleted = likeRepo.findByPostOrCommentIdAndLikeId(postOrCommentId,likeId);//to fetch the particular like
        likeRepo.deleteById(likeId);
        Like likeToBeDeleted = likeRepo.findByLikeId(likeId);
        return likeToBeDeleted;
    }

    @Override
    public Long getLikesCount(String postOrCommentId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("postOrCommentId").is(postOrCommentId));
        List<Like> likes = mongoTemplate.find(query, Like.class);
        long count = 0;
        for(Like like : likes){
            count++;
        }
        return count;
    }
}