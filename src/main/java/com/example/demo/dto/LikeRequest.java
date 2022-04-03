package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikeRequest {
    private String postOrCommentId;
    @NotEmpty(message = "Like should not be empty")
    private String likedBy;
}