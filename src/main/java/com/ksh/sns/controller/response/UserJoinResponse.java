package com.ksh.sns.controller.response;


import com.ksh.sns.model.User;
import com.ksh.sns.model.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserJoinResponse {

    private Integer id;
    private String email;
    private UserRole role;

    public static UserJoinResponse fromUser(User user) {
        return new UserJoinResponse(
                user.getId(),
                user.getEmail(),
                user.getUserRole()
        );
    }
}
