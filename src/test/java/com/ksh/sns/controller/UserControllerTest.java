package com.ksh.sns.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ksh.sns.controller.request.UserJoinRequest;
import com.ksh.sns.controller.request.UserLoginRequest;
import com.ksh.sns.exception.ErrorCode;
import com.ksh.sns.exception.SnsApplicationException;
import com.ksh.sns.model.User;
import com.ksh.sns.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserService userService;

    @Test
    @DisplayName("회원가입 성공")
    void signUp_success() throws Exception {
        String email ="test@gmail.com";
        String password = "1234";

        when(userService.join(email, password)).thenReturn(mock(User.class));

        mockMvc.perform(post("/api/v1/users/join")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsBytes(new UserJoinRequest(email, password)))
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("회원가입 시 중복된 이메일이 있는 경우 예외 반환")
    void signUp_fail() throws Exception {
        String email ="test@gmail.com";
        String password = "1234";

        when(userService.join(email, password)).thenThrow(new SnsApplicationException(ErrorCode.DUPLICATED_USER_EMAIL, ""));

        mockMvc.perform(post("/api/v1/users/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserJoinRequest(email, password)))
                ).andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("로그인 성공")
    void signIn_success() throws Exception {
        String email ="test@gmail.com";
        String password = "1234";

        when(userService.login(email, password)).thenReturn("test_token");

        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest(email, password)))
                ).andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("로그인 시 회원가입이 안된 이메일을 입력할 경우 예외 반환")
    void signIn_fail_email() throws Exception {
        String email ="test@gmail.com";
        String password = "1234";

        when(userService.login(email, password)).thenThrow(new SnsApplicationException(ErrorCode.USER_NOT_FOUND));

        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest(email, password)))
                ).andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("로그인 시 틀린 비밀번호를 입력할 경우 예외 반환")
    void signIn_fail_password() throws Exception {
        String email ="test@gmail.com";
        String password = "1234";

        when(userService.login(email, password)).thenThrow(new SnsApplicationException(ErrorCode.INVALID_PASSWORD));

        mockMvc.perform(post("/api/v1/users/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new UserLoginRequest(email, password)))
                ).andDo(print())
                .andExpect(status().isUnauthorized());
    }
}
