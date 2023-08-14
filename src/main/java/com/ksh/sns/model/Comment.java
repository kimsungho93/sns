package com.ksh.sns.model;

import com.ksh.sns.entity.CommentEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class Comment {
    private Integer id;
    private String comment;
    private String email;
    private Integer postId;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp deletedAt;

    public static Comment fromEntity(CommentEntity entity) {
        return new Comment(
                entity.getId(),
                entity.getComment(),
                entity.getUser().getEmail(),
                entity.getPost().getId(),
                entity.getRegisteredAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt()
        );
    }
}
