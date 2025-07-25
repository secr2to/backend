package com.emelmujiro.secreto.notification.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum NotificationType {

    INGAME_PROFILE_INFO("/sub/room/", "방 유저가 입장하였습니다."),
    USER_ACCEPT("/sub/room/", "방 유저가 수락되었습니다."),
    USER_REJECT("/sub/room/", "방 유저가 거절되었습니다."),
    INGAME_INTRODUCTION("/sub/room/", "방 유저의 자기소개가 수정되었습니다."),
    INGAME_PROFILE_IMAGE("/sub/room/", "방 유저의 프로필 이미지가 수정되었습니다."),
    ROOM_IMAGE("/sub/room/", "방 이미지가 수정되었습니다."),
    ROOM_INFORMATION("/sub/room/", "방 정보가 변경되었습니다."),
    ROOM_START("/sub/room/", "방이 시작되었습니다."),
    ROOM_END("/sub/room/", "방이 종료되었습니다."),
    MISSION("/sub/room/", "방에 미션이 제시되었습니다."),
    CHATTING("/sub/chatting/", "채팅이 작성되었습니다."),
    REPLY("/sub/user/", "댓글이 달렸습니다."),
    NESTED_REPLY("/sub/user/", "대댓글이 달렸습니다."),
    TAG("/sub/user/", "태그가 달렸습니다."),
    ;

    private final String subscribeUrl;
    private final String message;
}
