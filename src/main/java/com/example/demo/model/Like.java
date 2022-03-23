package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection="like")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Like {

    @Id
    private String likeId;
    private String postOrCommentId; //id can be postId or commentId
    private String likedBy; //a particular userId
    private Date createdAt;



}
