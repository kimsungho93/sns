package com.ksh.sns.model;

import com.ksh.sns.entity.PostEntity;
import com.ksh.sns.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class Post {

    private Integer id;

    private String title;

    private String content;

    private User user;

    private Timestamp registeredAt;

    private Timestamp updatedAt;

    private Timestamp deletedAt;

    public static Post fromEntity(PostEntity entity) {
        return new Post(
                entity.getId(),
                entity.getTitle(),
                entity.getContent(),
                User.fromEntity(entity.getUser()),
                entity.getRegisteredAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt()
        );
    }

}
