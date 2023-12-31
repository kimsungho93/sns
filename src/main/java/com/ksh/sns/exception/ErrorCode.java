package com.ksh.sns.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    DUPLICATED_USER_EMAIL(HttpStatus.CONFLICT, "중복된 이메일입니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 이메일의 가입된 유저를 찾을 수 없습니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "유효하지 않은 비밀번호입니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "내부 서버 에러입니다."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 유효하지 않습니다."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "포스트가 존재하지 않습니다."),
    INVALID_PERMISSION(HttpStatus.UNAUTHORIZED, "승인되지 않은 접근입니다."),
    ALREADY_LIKED(HttpStatus.CONFLICT, "이미 해당 포스트에 좋아요를 눌렀습니다."),
    ALARM_CONNECT_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "알람 연결에 실패하였습니다.");

    private final HttpStatus status;
    private final String message;
}
