package com.example.demo.repo;

import com.example.demo.model.Like;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataMongoTest
public class LikeRepoTest {

    @Autowired
    private LikeRepo likeRepo;

    @BeforeEach
    void initUseCase() {
        Like like = createLike();
        likeRepo.save(like);
    }

    @AfterEach
    public void destroyAll() {
        likeRepo.deleteAll();
    }

    @Test
    void testFindByLikeOnPost() {
        List<Like> like = likeRepo.findByPcId("1");
        assertEquals("1",like.get(0).getLikedBy());
    }

    @Test
    void testFindByLikedOnPost() {
        List<Like> like = likeRepo.findByPcId("1", PageRequest.of(0, 1));
        assertEquals("1",like.get(0).getPcId());

    }

    @Test
    void findByLikedOnPostAndCommentId() {
        Like like = likeRepo.findByPcIdAndId("1","1");
        assertEquals("1",like.getPcId());
        assertEquals("1",like.getLikedBy());

    }

    private Like createLike() {
        Like like =  new Like();
        like.setId("1");
        like.setLikedBy("1");
        like.setPcId("1");
        like.setCreatedAt(new Date());
        return like;
    }

}
