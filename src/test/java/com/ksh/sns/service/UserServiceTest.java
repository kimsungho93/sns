package com.ksh.sns.service;

import com.ksh.sns.entity.UserEntity;
import com.ksh.sns.exception.ErrorCode;
import com.ksh.sns.exception.SnsApplicationException;
import com.ksh.sns.fixture.UserEntityFixture;
import com.ksh.sns.repository.UserEntityRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserService userService;

    @MockBean
    private UserEntityRepository userEntityRepository;

    @MockBean
    private BCryptPasswordEncoder encoder;

    @Test
    @DisplayName("회원 가입이 정상적으로 동작하는 경우")
    void signUp_success() {
        String email = "test@gmail.com";
        String password = "1234";

        when(userEntityRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(encoder.encode(password)).thenReturn("encrypt_password");
        when(userEntityRepository.save(any())).thenReturn(UserEntityFixture.get(email, password));

        Assertions.assertDoesNotThrow(() -> userService.join(email, password));
    }

    @Test
    @DisplayName("회원 가입시 입력한 email로 회원가입한 유저가 있는 경우")
    void signUp_fail() {
        String email = "test@gmail.com";
        String password = "1234";

        UserEntity fixture = UserEntityFixture.get(email, password);

        when(userEntityRepository.findByEmail(email)).thenReturn(Optional.of(fixture));
        when(encoder.encode(password)).thenReturn("encrypt_password");
        when(userEntityRepository.save(any())).thenReturn(Optional.of(fixture));

        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class, () -> userService.join(email, password));
        Assertions.assertEquals(ErrorCode.DUPLICATED_USER_EMAIL, e.getErrorCode());
    }

    @Test
    @DisplayName("로그인이 정상적으로 동작하는 경우")
    void signIn_success() {
        String email = "test@gmail.com";
        String password = "1234";

        UserEntity fixture = UserEntityFixture.get(email, password);

        when(userEntityRepository.findByEmail(email)).thenReturn(Optional.of(fixture));
        when(encoder.matches(password, fixture.getPassword())).thenReturn(true);

        Assertions.assertDoesNotThrow(() -> userService.login(email, password));
    }

    @Test
    @DisplayName("로그인시 입력한 email로 가입한 유저가 없는 경우")
    void signIn_fail_email() {
        String email = "test@gmail.com";
        String password = "1234";


        when(userEntityRepository.findByEmail(email)).thenReturn(Optional.empty());

        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class, () -> userService.login(email, password));
        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }

    @Test
    @DisplayName("로그인시 입력한 password가 틀린 경우")
    void signIn_fail_password() {
        String email = "test@gmail.com";
        String password = "1234";
        String wrongPassword = "1111";

        UserEntity fixture = UserEntityFixture.get(email, password);

        when(userEntityRepository.findByEmail(email)).thenReturn(Optional.of(fixture));

        SnsApplicationException e = Assertions.assertThrows(SnsApplicationException.class, () -> userService.login(email, wrongPassword));
        Assertions.assertEquals(ErrorCode.INVALID_PASSWORD, e.getErrorCode());
    }
}
