package com.example.demo.feign;

import com.example.demo.dto.PostResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "post-service")
public interface PostFeign {
    @GetMapping("/posts/{postId}")
    public PostResponse getPostDetails(@PathVariable("postId") String postId);
}
