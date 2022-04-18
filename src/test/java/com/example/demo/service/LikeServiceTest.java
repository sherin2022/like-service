package com.example.demo.service;

import com.example.demo.constant.LikeConstant;
import com.example.demo.dto.LikeDto;
import com.example.demo.dto.LikeRequest;
import com.example.demo.dto.UserDto;
import com.example.demo.exception.CustomFeignException;
import com.example.demo.exception.LikeNotFoundException;
import com.example.demo.feign.UserFeign;
import com.example.demo.model.Like;
import com.example.demo.repo.LikeRepo;
import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class LikeServiceTest {

    @MockBean
    private LikeRepo likeRepo;

    @Autowired
    @InjectMocks
    private LikeServiceImpl likeServiceImpl;

    @MockBean
    private UserFeign userFeign;

    @Test
    void testRemoveLikeById() {
        when(this.userFeign.getUserDetails((String) any())).thenThrow(mock(FeignException.class));
        Like like = new Like();
        like.setId("1");
        like.setLikedBy("Liked By");
        like.setCreatedAt(new Date());
        like.setPcId("1");
        when(this.likeRepo.findByPcIdAndId((String) any(), (String) any())).thenReturn(like);
        doNothing().when(this.likeRepo).deleteById((String) any());
        assertThrows(CustomFeignException.class, () -> this.likeServiceImpl.removeLike("1", "1"));

    }

    @Test
    void testExceptionThrownWhenIdNotFound() {
        Mockito.doThrow(LikeNotFoundException.class).when(likeRepo).deleteById(any());
        Exception likeNotFoundException = assertThrows(LikeNotFoundException.class, () -> likeServiceImpl.removeLike("1", "1"));
        assertTrue(likeNotFoundException.getMessage().contains(LikeConstant.LIKE_NOT_FOUND_FOR_POST_OR_COMMENT));
    }

    @Test
    void testGetLikesList() {
        UserDto userDto = new UserDto();
        when(this.userFeign.getUserDetails((String) any())).thenReturn(userDto);
        Like like = new Like();
        like.setId("1");
        like.setLikedBy("Liked By");
        like.setCreatedAt(new Date());
        like.setPcId("1");

        ArrayList<Like> likeList = new ArrayList<>();
        likeList.add(like);
        when(this.likeRepo.findByPcId((String) any(), (org.springframework.data.domain.Pageable) any())).thenReturn(likeList);
        List<LikeDto> actualLikes = this.likeServiceImpl.getLikes("1", 1, 3);
        assertEquals(1, actualLikes.size());
        LikeDto getResult = actualLikes.get(0);
        assertEquals("1", getResult.getId());
        assertEquals("1", getResult.getPostOrCommentId());
        assertEquals(new Date(), new Date());
        assertSame(userDto, getResult.getLikedBy());

    }

    @Test
    void testExceptionThrownWhenLikeNotFoundByPostOrCommentId() {
        when(this.likeRepo.findByPcId((String) any(), (org.springframework.data.domain.Pageable) any())).thenReturn(new ArrayList<>());
        assertThrows(LikeNotFoundException.class, () -> this.likeServiceImpl.getLikes("1", 1, 3));

    }

    @Test
    void testExceptionThrownForFeignConnectionIssueWhenGetAllLikes() {
        when(this.userFeign.getUserDetails((String) any())).thenThrow(mock(FeignException.class));
        Like like = new Like();
        like.setCreatedAt(new Date());
        like.setId("1");
        like.setLikedBy("Feign Test");
        like.setPcId("1");

        ArrayList<Like> likeList = new ArrayList<>();
        likeList.add(like);
        when(this.likeRepo.findByPcId((String) any(), (org.springframework.data.domain.Pageable) any())).thenReturn(likeList);
        assertThrows(CustomFeignException.class, () -> this.likeServiceImpl.getLikes("1", 1, 3));

    }

    @Test
    void getLikesCountById() {
        List<Like> likes = new ArrayList<>();
        Like like1 = new Like();
        like1.setId("1");
        like1.setLikedBy("1");

        Like like2 = new Like();
        like2.setId("2");
        like2.setLikedBy("nice");

        likes.add(like1);
        likes.add(like2);
        Mockito.when(likeRepo.findByPcIdAndId("1", "1")).thenReturn(like1);
        assertThat(likeServiceImpl.getLikesCount("1"));
    }

    @Test
    void testExceptionThrownWhenLikesNotFoundById() {
        Mockito.doThrow(LikeNotFoundException.class).when(likeRepo).findByPcId(any());
        Exception likeNotFoundException = assertThrows(LikeNotFoundException.class, () -> likeServiceImpl.getLikesCount("1"));
        assertTrue(likeNotFoundException.getMessage().contains(LikeConstant.LIKE_NOT_FOUND_FOR_POST_OR_COMMENT));
    }

    @Test
    void testGetLikeDetailsById() {
        when(this.userFeign.getUserDetails((String) any())).thenThrow(mock(FeignException.class));
        Like like = new Like();
        like.setId("1");
        like.setLikedBy("Liked By");
        like.setCreatedAt(new Date());
        like.setPcId("1");
        when(this.likeRepo.findByPcIdAndId((String) any(), (String) any())).thenReturn(like);
        assertThrows(CustomFeignException.class, () -> this.likeServiceImpl.getLikeDetails("1", "1"));

    }

    @Test
    void testCreateLike() {
        UserDto userDto = new UserDto();
        when(this.userFeign.getUserDetails((String) any())).thenReturn(userDto);

        Like like = new Like();
        like.setId("1");
        like.setLikedBy("Liked By");
        like.setCreatedAt(new Date());
        like.setPcId("1");
        when(this.likeRepo.save((Like) any())).thenReturn(like);
        LikeDto actualCreateLikeResult = this.likeServiceImpl.createLike("1", new LikeRequest("1", "Liked By"));
        assertNull(actualCreateLikeResult.getId());
        assertEquals("1", actualCreateLikeResult.getPostOrCommentId());
        assertSame(userDto, actualCreateLikeResult.getLikedBy());

    }

    @Test
    void testExceptionThrownWhenFeignConnectionIssueForCreateLike() {
        LikeRequest likeRequest = new LikeRequest("1", "Liked By");
        when(this.userFeign.getUserDetails((String) any())).thenReturn(new UserDto());
        when(this.likeRepo.save((Like) any())).thenThrow(mock(FeignException.class));
        assertThrows(CustomFeignException.class, () -> this.likeServiceImpl.createLike("1", likeRequest));

    }

}