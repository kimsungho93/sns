package com.ksh.sns.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ksh.sns.controller.request.PostCreateRequest;
import com.ksh.sns.controller.request.PostModifyRequest;
import com.ksh.sns.controller.request.UserJoinRequest;
import com.ksh.sns.exception.ErrorCode;
import com.ksh.sns.exception.SnsApplicationException;
import com.ksh.sns.fixture.PostEntityFixture;
import com.ksh.sns.model.Post;
import com.ksh.sns.service.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PostService postService;

    @Test
    @DisplayName("포스트 작성 성공")
    @WithMockUser
    void postWrite_success() throws Exception{
        String title = "제목1";
        String content = "내용1";

        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostCreateRequest(title, content)))
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("포스트 작성시 로그인 하지 않은 경우")
    @WithAnonymousUser
    void postWrite_fail() throws Exception{
        String title = "제목1";
        String content = "내용1";

        mockMvc.perform(post("/api/v1/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostCreateRequest(title, content)))
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("포스트 수정 성공")
    @WithMockUser
    void postModify_success() throws Exception{
        String title = "제목1";
        String content = "내용1";

        when(postService.modify(eq(title), eq(content), eq(any()), any())).
                thenReturn(Post.fromEntity(PostEntityFixture.get("test@gmail.com", 1, 1)));

        mockMvc.perform(put("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostModifyRequest(title, content)))
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("포스트 수정시 로그인 하지 않은 경우")
    @WithAnonymousUser
    void postModify_fail() throws Exception{
        String title = "제목1";
        String content = "내용1";

        mockMvc.perform(put("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostModifyRequest(title, content)))
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("포스트 수정시 본인이 작성한 포스트가 아닌 경우 예외 발생")
    @WithMockUser
    void postModify_fail_not_write() throws Exception{
        String title = "제목1";
        String content = "내용1";

        doThrow(new SnsApplicationException(ErrorCode.INVALID_PERMISSION)).when(postService).modify(eq(title), eq(content), any(), eq(1));

        mockMvc.perform(put("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostModifyRequest(title, content)))
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }


    @Test
    @DisplayName("포스트 수정시 수정 하려는 포스트가 없는 경우 예외 발생")
    @WithMockUser
    void postModify_fail_not_found_post() throws Exception{
        String title = "제목1";
        String content = "내용1";

        doThrow(new SnsApplicationException(ErrorCode.POST_NOT_FOUND)).when(postService).modify(eq(title), eq(content), any(), eq(1));

        mockMvc.perform(put("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new PostModifyRequest(title, content)))
                ).andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("포스트 삭제 성공")
    @WithMockUser
    void postDelete_success() throws Exception{

        mockMvc.perform(delete("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("포스트 삭제시 로그인하지 않은 경우")
    @WithAnonymousUser
    void postDelete_fail_not_login() throws Exception{

        mockMvc.perform(delete("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("포스트 삭제시 작성자와 삭제 요청자가 다른 경우")
    @WithMockUser
    void postDelete_fail_not_match_user() throws Exception{
        // mocking
        doThrow(new SnsApplicationException(ErrorCode.INVALID_PERMISSION)).when(postService).delete(any(), any());

        mockMvc.perform(delete("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("포스트 삭제시 삭제하려는 포스트가 존재하지 않는 경우")
    @WithMockUser
    void postDelete_fail_none_exist_post() throws Exception{
        // mocking
        doThrow(new SnsApplicationException(ErrorCode.POST_NOT_FOUND)).when(postService).delete(any(), any());

        mockMvc.perform(delete("/api/v1/posts/1")
                        .contentType(MediaType.APPLICATION_JSON)
                ).andDo(print())
                .andExpect(status().isNotFound());
    }



}
