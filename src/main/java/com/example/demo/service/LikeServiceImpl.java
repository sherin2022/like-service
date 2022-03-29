package com.example.demo.service;

import com.example.demo.dto.LikeDto;
import com.example.demo.dto.LikeRequest;
import com.example.demo.feign.PostFeign;
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

import static com.example.demo.constant.LikeConstant.LIKE_DELETED;


@Service
public class LikeServiceImpl implements LikeService {
    @Autowired
    LikeRepo likeRepo;

    @Autowired
    UserFeign userFeign;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    PostFeign postFeign;


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
    public LikeDto getLikeDetails(String postOrCommentId, String likeId) {
        Like like = likeRepo.findByLikeId(likeId);
        String userName = userFeign.getUserById(like.getLikedBy()).getBody().getFirstName();
        String post = postFeign.getPostDetails(postOrCommentId).getPost();
        LikeDto likeDto = new LikeDto(like.getLikeId(),like.getPostOrCommentId(),like.getLikedBy(),like.getCreatedAt(),userName,post);
        return likeDto;
    }

    @Override
    public Integer getLikesCount(String postOrCommentId) {
        Query query = new Query();
        query.addCriteria(Criteria.where("postOrCommentId").is(postOrCommentId));
        List<Like> likes = mongoTemplate.find(query, Like.class);
        return likes.size();
    }

    @Override
    public String deleteLike(String likeId) {
         likeRepo.deleteById(likeId);
         return LIKE_DELETED;

    }
}