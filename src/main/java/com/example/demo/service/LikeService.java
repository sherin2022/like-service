package com.example.demo.service;

import com.example.demo.dto.LikeDto;
import com.example.demo.dto.LikeRequest;
import com.example.demo.model.Like;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LikeService {

    List<Like> getLikes(String postOrCommentId);
    Like createLike(String postOrCommentId, LikeRequest likeRequest);
    LikeDto getLikeDetails(String likeId);
    Like removeLike(String likeId);
    Long getLikesCount(String postOrCommentId);
}
