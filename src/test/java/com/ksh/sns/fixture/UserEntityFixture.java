package com.ksh.sns.fixture;

import com.ksh.sns.entity.UserEntity;

public class UserEntityFixture {

    public static UserEntity get(String email, String password, Integer userId) {
        UserEntity result = new UserEntity();
        result.setId(1);
        result.setEmail(email);
        result.setPassword(password);
        return result;
    }
}
