package com.ksh.sns.service;

import com.ksh.sns.entity.PostEntity;
import com.ksh.sns.entity.UserEntity;
import com.ksh.sns.exception.ErrorCode;
import com.ksh.sns.exception.SnsApplicationException;
import com.ksh.sns.repository.PostEntityRepository;
import com.ksh.sns.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostEntityRepository postEntityRepository;
    private final UserEntityRepository userEntityRepository;

    @Transactional
    public void create(String title, String content, String email) {
        UserEntity userEntity = userEntityRepository.findByEmail(email).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", email)));
        postEntityRepository.save(PostEntity.of(title, content, userEntity));
    }
}
