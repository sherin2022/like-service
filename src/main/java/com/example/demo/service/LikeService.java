package com.example.demo.service;

import com.example.demo.dto.LikeDto;
import com.example.demo.dto.LikeRequest;
import com.example.demo.model.Like;

import java.util.List;
import java.util.Map;

public interface LikeService {
    List<Like> getLikes(String postOrCommentId);

    Like createLike(String postOrCommentId, LikeRequest likeRequest);

    LikeDto getLikeDetails(String postOrCommentId, String likeId);

    Map<String,Like> removeLike(String postOrCommentId, String likeId);

    int getLikesCount(String postOrCommentId);
}
