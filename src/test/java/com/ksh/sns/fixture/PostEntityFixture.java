package com.ksh.sns.fixture;

import com.ksh.sns.entity.PostEntity;
import com.ksh.sns.entity.UserEntity;

public class PostEntityFixture {

    public static PostEntity get(String email, Integer postId, Integer userId) {
        UserEntity user = new UserEntity();
        user.setId(userId);
        user.setEmail(email);

        PostEntity result = new PostEntity();
        result.setUser(user);
        result.setId(postId);
        return result;
    }
}
