package com.example.demo.controller;

import com.example.demo.dto.LikeDto;
import com.example.demo.dto.LikeRequest;
import com.example.demo.dto.UserDto;
import com.example.demo.exception.PostIdMismatchException;
import com.example.demo.model.Like;
import com.example.demo.service.LikeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@WebMvcTest(LikeController.class)
public class LikeControllerTest {

    @MockBean
    LikeService likeService;

    @Autowired
    MockMvc mockMvc;

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testGetAllLikes()throws Exception {
        List<LikeDto> likeDto = createLikeList();
        Mockito.when(likeService.getLikes("1", 1, 2)).thenReturn(likeDto);
        mockMvc.perform(get("/postsOrComments/1/likes?page=1&pageSize=2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].postOrCommentId", Matchers.is("TestOne")))
                .andExpect(jsonPath("$[1].postOrCommentId", Matchers.is("TestTwo")));
    }

    @Test
    void testCreateLike() throws Exception {
        LikeDto likeDto = new LikeDto();
        LikeRequest likeRequest = createOneLikeRequestToPost();
        Mockito.when(likeService.createLike("1",likeRequest)).thenReturn(likeDto);
        mockMvc.perform(post("/postsOrComments/1/likes")
                        .content(asJsonString(likeRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    void testExceptionThrownWhenPostOrCommentIdNotFound() throws Exception {
        LikeController likeController = new LikeController();
        LikeRequest likeRequest = mock(LikeRequest.class);
        when(likeRequest.getPostOrCommentId()).thenReturn("a");
        assertThrows(NullPointerException.class, () -> likeController.createLike("1", likeRequest));

    }

    @Test
    void testGetLikesByID() throws Exception {
        LikeDto likeDto = createOneLike();
        Mockito.when(likeService.getLikeDetails("1","2")).thenReturn(likeDto);
        mockMvc.perform(get("/postsOrComments/1/likes/2"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.aMapWithSize(4)))
                .andExpect(jsonPath("$.postOrCommentId", Matchers.is("LikeTest")));
    }

    @Test
    void testRemoveLikeById() throws Exception {
        LikeDto likeDto = createOneLike();
        when(likeService.removeLike("1","2")).thenReturn(likeDto);
        this.mockMvc.perform(MockMvcRequestBuilders
                        .delete("/postsOrComments/1/likes/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void testGetLikesCountById() throws Exception {
        Integer count = createLikesToCount();
        Mockito.when(likeService.getLikesCount("1")).thenReturn(count);
        mockMvc.perform(get("/postsOrComments/1/likes/count"))
                .andDo(print())
                .andExpect(status().isOk());
    }

    private LikeDto createOneLike() {
        LikeDto likeDto = new LikeDto();
        likeDto.setId("1");
        likeDto.setPostOrCommentId("LikeTest");
        likeDto.setLikedBy(new UserDto("1","Savita","J","S","9090909090","Savita@mail.com", null,"123",null,null));
        return likeDto;
    }

    private LikeRequest createOneLikeRequestToPost() {
        LikeRequest likeRequest = new LikeRequest();
        likeRequest.setLikedBy("1");
        likeRequest.setPostOrCommentId("1");
        likeRequest.setLikedBy(String.valueOf(new UserDto("1","Savita","J","S","9090909090","Savita@mail.com", null,"123",null,null)));
        return likeRequest;

    }

    private Like createOneLikeToPost() {
        Like like = new Like();
        like.setId("1");
        like.setPcId("1");
        like.setLikedBy(String.valueOf(new UserDto("1","Savita","J","S","9090909090","Savita@mail.com", null,"123",null,null)));
        return like;
    }

    private Integer createLikesToCount() {
        List<Like> likes = new ArrayList<>();

        Like like1 = new Like();
        Like like2 = new Like();
        Like like3 = new Like();

        likes.add(like1);
        likes.add(like2);
        likes.add(like3);
        return likes.size();
    }

    private List<LikeDto> createLikeList() {
        List<LikeDto> likeDto = new ArrayList<>();

        LikeDto likeDto1 = new LikeDto();
        likeDto1.setId("1");
        likeDto1.setPostOrCommentId("TestOne");
        likeDto1.setLikedBy(new UserDto("1","Savita","J","S","9090909090","Savita@mail.com", null,"123",null,null));
        likeDto1.setCreatedAt(new Date());

        LikeDto likeDto2 = new LikeDto();
        likeDto2.setId("2");
        likeDto2.setPostOrCommentId("TestTwo");
        likeDto2.setLikedBy(new UserDto("1","Savita","J","S","9090909090","Savita@mail.com", null,"123",null,null));
        likeDto2.setCreatedAt(new Date());

        likeDto.add(likeDto1);
        likeDto.add(likeDto2);
        return likeDto;
    }
}
