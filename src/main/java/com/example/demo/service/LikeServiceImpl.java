package com.example.demo.service;
import com.example.demo.dto.LikeDto;
import com.example.demo.dto.LikeRequest;
import com.example.demo.exception.LikeNotFoundException;
import com.example.demo.feign.UserFeign;
import com.example.demo.model.Like;
import com.example.demo.repo.LikeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static com.example.demo.constant.LikeConstant.deleteLike;


@Service
public class LikeServiceImpl implements LikeService {
    @Autowired
    LikeRepo likeRepo;

    @Autowired
    UserFeign userFeign;
    @Override
    public List<Like> getLikes(String postOrCommentId) {
        return likeRepo.findByPcId(postOrCommentId);
    }

    @Override
    public Like createLike(String postOrCommentId, LikeRequest likeRequest) {
        Like like = new Like();
        like.setPcId(likeRequest.getPostOrCommentId());
        like.setLikedBy(likeRequest.getLikedBy());
        like.setLocalDate(LocalDate.now());
        return likeRepo.save(like);
    }


    public LikeDto getLikeDetails(String postOrCommentId, String likeId) {
        Optional<Like> li = likeRepo.findById(likeId);

        if (li.isPresent()) {
            Like like = li.get();
          //  String userName = userFeign.getUserById(like.getLikedBy()).getBody().getFirstName();
          //  String userName = userFeign.getUserById(like.getUserName());
           // like.setLikedBy(userName);
           // return new LikeDto(like.getId(), like.getPcId(), userFeign.getUserById(like.getLikedBy()).getBody().getFirstName(), like.getLocalDate());
            return new LikeDto(like.getId(),like.getPcId(),like.getLikedBy(),like.getLocalDate(),like.getUserName());
        } else {
            throw new LikeNotFoundException("No like found : " + likeId);
        }
    }


    @Override
    public Map<String,Like> removeLike(String postOrCommentId, String likeId) {
        Like like = likeRepo.findByPcIdAndId(postOrCommentId,likeId);
        likeRepo.deleteById(likeId);
        Map<String,Like> body = new HashMap<>();
        body.put(deleteLike,like);
        return body;
    }

    @Override
    public int getLikesCount(String postOrCommentId) {
        List<Like> likes = likeRepo.findByPcId(postOrCommentId);
        int count = 0;
        for(Like like : likes){
            count++;
        }
        return count;
    }
}
