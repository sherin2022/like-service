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
import java.util.ArrayList;
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
    public List<LikeDto> getLikes(String postOrCommentId) {

        Query query = new Query();
        query.addCriteria(Criteria.where("postOrCommentId").is(postOrCommentId));
        List<Like> listOfLikes = mongoTemplate.find(query, Like.class);
        List<LikeDto> likesDto = new ArrayList<>();
        for(Like like:listOfLikes){
            LikeDto likeDto= new LikeDto(like.getLikeId(),like.getPostOrCommentId(),userFeign.getUserDetails(like.getLikedBy()),like.getCreatedAt());
            likesDto.add(likeDto);
        }
        return likesDto;
    }

    @Override
    public LikeDto createLike(String postOrCommentId, LikeRequest likeRequest) {
        Like like = new Like();
        like.setPostOrCommentId(postOrCommentId);
        like.setLikedBy(likeRequest.getLikedBy());
        like.setCreatedAt(new Date());
        likeRepo.save(like);
        return new LikeDto(like.getLikeId(),like.getPostOrCommentId(),userFeign.getUserDetails(like.getLikedBy()),like.getCreatedAt());

    }

    @Override
    public LikeDto getLikeDetails(String postOrCommentId, String likeId) {
        Like like = likeRepo.findByLikeId(likeId);
        return new LikeDto(like.getLikeId(),like.getPostOrCommentId(),userFeign.getUserDetails(like.getLikedBy()),like.getCreatedAt());

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