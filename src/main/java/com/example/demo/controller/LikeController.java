package com.example.demo.controller;

import com.example.demo.dto.LikeDto;
import com.example.demo.dto.LikeRequest;
import com.example.demo.model.Like;
import com.example.demo.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/postsOrComments")
public class LikeController {
    @Autowired
    LikeService likeService;

    @GetMapping("/{postOrCommentId}/likes")
    public ResponseEntity<List<Like>> getLikes(@PathVariable("postOrCommentId") String postOrCommentId){
        return new ResponseEntity<>(likeService.getLikes(postOrCommentId), HttpStatus.OK);
    }

    @PostMapping("/{postOrCommentId}/likes")
    public ResponseEntity<Like> createLike(@PathVariable("postOrCommentId") String postOrCommentId,@RequestBody LikeRequest likeRequest){
        return new ResponseEntity<>(likeService.createLike(postOrCommentId,likeRequest), HttpStatus.CREATED);
    }
    @DeleteMapping("/{postOrCommentId}/likes/{likeId}")
    public ResponseEntity<Like> removeLike(@PathVariable("postOrCommentId") String postOrCommentId, @PathVariable("likeId") String likeId){
        return new ResponseEntity<>(likeService.removeLike(likeId), HttpStatus.OK);
    }
    @GetMapping("/{postOrCommentId}/likes/count")
    public ResponseEntity<Long> getLikesCount(@PathVariable("postOrCommentId") String postOrCommentId){
        return new ResponseEntity<>(likeService.getLikesCount(postOrCommentId), HttpStatus.OK);
    }
}
