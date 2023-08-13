package com.ksh.sns.repository;

import com.ksh.sns.entity.PostEntity;
import com.ksh.sns.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostEntityRepository extends JpaRepository<PostEntity, Integer> {
    Page<PostEntity> findAllByUser(UserEntity entity, Pageable pageable);
}
