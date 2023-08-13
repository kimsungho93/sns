package com.ksh.sns.controller;

import com.ksh.sns.controller.request.UserJoinRequest;
import com.ksh.sns.controller.request.UserLoginRequest;
import com.ksh.sns.controller.response.Response;
import com.ksh.sns.controller.response.UserJoinResponse;
import com.ksh.sns.controller.response.UserLoginResponse;
import com.ksh.sns.model.User;
import com.ksh.sns.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @PostMapping("/join")
    public Response<UserJoinResponse> join(@RequestBody UserJoinRequest request) {
        return Response.success(UserJoinResponse.fromUser(userService.join(request.getEmail(), request.getPassword())));
    }

    @PostMapping("/login")
    public Response<UserLoginResponse> login(@RequestBody UserLoginRequest request) {
        String token = userService.login(request.getEmail(), request.getPassword());
        return Response.success(new UserLoginResponse(token));
    }
}
