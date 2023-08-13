package com.ksh.sns.service;

import com.ksh.sns.entity.PostEntity;
import com.ksh.sns.entity.UserEntity;
import com.ksh.sns.exception.ErrorCode;
import com.ksh.sns.exception.SnsApplicationException;
import com.ksh.sns.repository.PostEntityRepository;
import com.ksh.sns.repository.UserEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PostServiceTest {

    @Autowired
    private PostService postService;

    @MockBean
    private PostEntityRepository postEntityRepository;

    @MockBean
    private UserEntityRepository userEntityRepository;

    @Test
    @DisplayName("포스트 작성이 성공한 경우")
    void posting_success() {
        String title = "제목1";
        String content = "내용1";
        String email = "test@gmail.com";

        when(userEntityRepository.findByEmail(email)).thenReturn(Optional.of(mock(UserEntity.class)));
        when(postEntityRepository.save(any())).thenReturn(mock(PostEntity.class));

        Assertions.assertDoesNotThrow(() -> postService.create(title, content, email));
    }

    @Test
    @DisplayName("포스트 작성시 요청한 유저가 존재 하지 않는 경우")
    void posting_fail_none_existent_user() {
        String title = "제목1";
        String content = "내용1";
        String email = "test@gmail.com";

        when(userEntityRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(postEntityRepository.save(any())).thenReturn(mock(PostEntity.class));

        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class, () -> postService.create(title, content, email));
        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }
}
