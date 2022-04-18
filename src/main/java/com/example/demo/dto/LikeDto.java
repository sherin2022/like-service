package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikeDto {

    private String id;
    private String postOrCommentId;
    private UserDto likedBy;
    private Date createdAt;

}

