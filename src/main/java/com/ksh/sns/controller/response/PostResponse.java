package com.ksh.sns.controller.response;

import com.ksh.sns.model.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class PostResponse {
    private Integer id;

    private String title;

    private String content;

    private UserResponse user;

    private Timestamp registeredAt;

    private Timestamp updatedAt;

    private Timestamp deletedAt;

    public static PostResponse fromPost(Post post) {
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                UserResponse.fromUser(post.getUser()),
                post.getRegisteredAt(),
                post.getUpdatedAt(),
                post.getDeletedAt()
        );
    }
}
