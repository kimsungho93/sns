package com.ksh.sns.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum AlarmType {
    NEW_COMMENT("새로운 댓글이 달렸습니다!"),
    NEW_LIKE("좋아요가 눌렸습니다!");


    private final String alarmText;
}
