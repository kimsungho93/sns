package com.ksh.sns.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ksh.sns.controller.request.PostCreateRequest;
import com.ksh.sns.controller.request.UserJoinRequest;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
}
