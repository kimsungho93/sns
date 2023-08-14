package com.ksh.sns.repository;

import com.ksh.sns.entity.LikeEntity;
import com.ksh.sns.entity.PostEntity;
import com.ksh.sns.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeEntityRepository extends JpaRepository<LikeEntity, Integer> {
    Optional<LikeEntity> findByUserAndPost(UserEntity user, PostEntity post);

//    @Query(value = "SELECT COUNT(*) FROM LikeEntity entity WHERE entity.post = :post")
//    Integer countByPost(@Param("post") PostEntity post);

    long countByPost(PostEntity post);

}
