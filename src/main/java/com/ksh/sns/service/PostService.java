package com.ksh.sns.service;

import com.ksh.sns.entity.LikeEntity;
import com.ksh.sns.entity.PostEntity;
import com.ksh.sns.entity.UserEntity;
import com.ksh.sns.exception.ErrorCode;
import com.ksh.sns.exception.SnsApplicationException;
import com.ksh.sns.model.Post;
import com.ksh.sns.repository.LikeEntityRepository;
import com.ksh.sns.repository.PostEntityRepository;
import com.ksh.sns.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostEntityRepository postEntityRepository;
    private final UserEntityRepository userEntityRepository;
    private final LikeEntityRepository likeEntityRepository;

    @Transactional
    public void create(String title, String content, String email) {
        UserEntity userEntity = userEntityRepository.findByEmail(email).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", email)));
        postEntityRepository.save(PostEntity.of(title, content, userEntity));
    }

    @Transactional
    public Post modify(String title, String content, String email, Integer postId) {
        UserEntity userEntity = userEntityRepository.findByEmail(email).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", email)));

        PostEntity postEntity = postEntityRepository.findById(postId).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s not founded", postId)));

        if (postEntity.getUser() != userEntity) {
            throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with %s", email, postId));
        }

        postEntity.setTitle(title);
        postEntity.setContent(content);

        return Post.fromEntity(postEntityRepository.saveAndFlush(postEntity));
    }

    @Transactional
    public void delete(String email, Integer postId) {
        UserEntity userEntity = userEntityRepository.findByEmail(email).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", email)));

        PostEntity postEntity = postEntityRepository.findById(postId).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s not founded", postId)));

        if (postEntity.getUser() != userEntity) {
            throw new SnsApplicationException(ErrorCode.INVALID_PERMISSION, String.format("%s has no permission with %s", email, postId));
        }

        postEntityRepository.delete(postEntity);
    }

    public Page<Post> list(Pageable pageable) {
        return postEntityRepository.findAll(pageable).map(Post::fromEntity);
    }

    public Page<Post> my(String email, Pageable pageable) {
        UserEntity userEntity = userEntityRepository.findByEmail(email).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", email)));

        return postEntityRepository.findAllByUser(userEntity, pageable).map(Post::fromEntity);
    }

    @Transactional
    public void like(Integer postId, String email) {
        PostEntity postEntity = postEntityRepository.findById(postId).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s not founded", postId)));

        UserEntity userEntity = userEntityRepository.findByEmail(email).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.USER_NOT_FOUND, String.format("%s not founded", email)));

        likeEntityRepository.findByUserAndPost(userEntity, postEntity).ifPresent(it -> {
            throw new SnsApplicationException(ErrorCode.ALREADY_LIKED, String.format("email : %s already like post %d", email, postId));
        });

        likeEntityRepository.save(LikeEntity.of(userEntity, postEntity));
    }

    @Transactional
    public int likeCount(Integer postId) {
        PostEntity postEntity = postEntityRepository.findById(postId).orElseThrow(() ->
                new SnsApplicationException(ErrorCode.POST_NOT_FOUND, String.format("%s not founded", postId)));

        return likeEntityRepository.countByPost(postEntity);
    }
}
