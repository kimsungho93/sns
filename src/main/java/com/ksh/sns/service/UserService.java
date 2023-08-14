package com.ksh.sns.service;

import com.ksh.sns.entity.UserEntity;
import com.ksh.sns.exception.ErrorCode;
import com.ksh.sns.exception.SnsApplicationException;
import com.ksh.sns.model.Alarm;
import com.ksh.sns.model.User;
import com.ksh.sns.repository.AlarmEntityRepository;
import com.ksh.sns.repository.UserCacheRepository;
import com.ksh.sns.repository.UserEntityRepository;
import com.ksh.sns.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserEntityRepository userEntityRepository;
    private final AlarmEntityRepository alarmEntityRepository;
    private final UserCacheRepository userCacheRepository;
    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.token.expired-time-ms}")
    private Long expiredTimeMs;

    public User loadUserByEmail(String email) {
        return userCacheRepository.getUser(email).orElseGet(() ->
                userEntityRepository.findByEmail(email).map(User::fromEntity).orElseThrow(
                () -> new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", email))));
    }

    // 회원가입
    @Transactional
    public User join(String email, String password) {
        // 중복된 이메일 체크
        userEntityRepository.findByEmail(email).ifPresent(it -> {
            throw new SnsApplicationException(ErrorCode.DUPLICATED_USER_EMAIL, String.format("Email is %s", email));
        });

        UserEntity savedUser = userEntityRepository.save(UserEntity.of(email, encoder.encode(password)));
        return User.fromEntity(savedUser);
    }

    // 로그인
    public String login(String email,String password) {
        // 회원가입 여부 체크
        User user = loadUserByEmail(email);
        userCacheRepository.setUser(user);

        // 비밀번호 체크
        if (!encoder.matches(password, user.getPassword())) {
            throw new SnsApplicationException(ErrorCode.INVALID_PASSWORD);
        }

        // 토큰 생성
        String token = JwtTokenUtils.generateToken(email, secretKey, expiredTimeMs);

        return token;
    }

    public Page<Alarm> alarmList(Integer userId, Pageable pageable) {
        return alarmEntityRepository.findAllByUserId(userId, pageable).map(Alarm::fromEntity);
    }
}
