package com.example.demo.service;

import com.example.demo.dto.LikeDto;
import com.example.demo.dto.LikeRequest;
import com.example.demo.exception.CustomFeignException;
import com.example.demo.exception.LikeNotFoundException;
import com.example.demo.feign.PostFeign;
import com.example.demo.feign.UserFeign;
import com.example.demo.model.Like;
import com.example.demo.repo.LikeRepo;
import com.netflix.hystrix.exception.HystrixRuntimeException;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.example.demo.constant.LikeConstant.*;


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
    public List<LikeDto> getLikes(String postOrCommentId, Integer page, Integer pageSize) {

        try {
            if (page == null) {
                page = 1;
            }
            if (pageSize == null) {
                pageSize = 10;
            }
            List<Like> likes = likeRepo.findByPcId(postOrCommentId, PageRequest.of(page - 1, pageSize));
            List<LikeDto> likeDtoList = new ArrayList<>();
        for(Like like:likes){
            LikeDto likeDto= new LikeDto(like.getId(),like.getPcId(),userFeign.getUserDetails(like.getLikedBy()),like.getCreatedAt());
            likeDtoList.add(likeDto);
        }
            if (likeDtoList.isEmpty()) {
                throw new LikeNotFoundException(LIKE_NOT_FOUND_FOR_POST_OR_COMMENT + postOrCommentId);
            }
        return likeDtoList;
        } catch (FeignException | HystrixRuntimeException e) {
            throw new CustomFeignException(FEIGN_EXCEPTION);
        }
    }

    @Override
    public LikeDto createLike(String postOrCommentId, LikeRequest likeRequest) {
        try {
            Like like = new Like();
            like.setPcId(postOrCommentId);
            like.setLikedBy(likeRequest.getLikedBy());
            like.setCreatedAt(new Date());
            likeRepo.save(like);
            return new LikeDto(like.getId(), like.getPcId(), userFeign.getUserDetails(like.getLikedBy()), like.getCreatedAt());
        } catch (FeignException | HystrixRuntimeException e) {
            throw new CustomFeignException(FEIGN_EXCEPTION);
        }

    }

    @Override
    public LikeDto getLikeDetails(String postOrCommentId, String likeId) {
        try {
            Like like = likeRepo.findByPcIdAndId(postOrCommentId, likeId);
            if (like == null) {
                throw new LikeNotFoundException(LIKE_NOT_FOUND_FOR_POST_OR_COMMENT + postOrCommentId + LIKE_ID + likeId);
            }
            return new LikeDto(like.getId(), like.getPcId(), userFeign.getUserDetails(like.getLikedBy()), like.getCreatedAt());
        } catch (FeignException | HystrixRuntimeException e) {
            throw new CustomFeignException(FEIGN_EXCEPTION);
        }

    }

    @Override
    public Integer getLikesCount(String postOrCommentId) {
        try {
            List<Like> likes = likeRepo.findByPcId(postOrCommentId);
            return likes.size();
        } catch (Exception e) {
            throw new LikeNotFoundException(LIKE_NOT_FOUND_FOR_POST_OR_COMMENT + postOrCommentId);
        }
    }

    @Override
    public LikeDto removeLike(String postOrCommentId, String likeId) {
        try {
            Like like = likeRepo.findByPcIdAndId(postOrCommentId, likeId);
            if (like == null) {
                throw new LikeNotFoundException(LIKE_NOT_FOUND_FOR_POST_OR_COMMENT + postOrCommentId + LIKE_ID + likeId);
            }
            likeRepo.deleteById(likeId);
            return new LikeDto(like.getId(), like.getPcId(), userFeign.getUserDetails(like.getLikedBy()), like.getCreatedAt());
        } catch (FeignException | HystrixRuntimeException e) {
            throw new CustomFeignException(FEIGN_EXCEPTION);
        }
    }
}