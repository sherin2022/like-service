package com.example.demo.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LikeDto {
    private String likeId;
    private String postOrCommentId;
    private String likedBy;
    private LocalDate localDate;

}