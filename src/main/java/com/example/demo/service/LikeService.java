package com.example.demo.service;

import com.example.demo.dto.LikeDto;
import com.example.demo.dto.LikeRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface LikeService {

    List<LikeDto> getLikes(String postOrCommentId);
    LikeDto createLike(String postOrCommentId, LikeRequest likeRequest);
    LikeDto getLikeDetails(String postOrCommentId, String likeId);
    Integer getLikesCount(String postOrCommentId);
    String deleteLike(String likeId);
}
