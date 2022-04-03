package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;


@Document(collection = "Like")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Like {
    @Id
    private String id;
    private String pcId; //id can be postId or commentId
    private String likedBy; //a particular userId
    private LocalDate createdAt;

}