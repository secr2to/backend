package com.emelmujiro.secreto.chatting.dto;

import lombok.*;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class GetChattingListReqDto {

    private Long roomId;

    private Long userId;

    private String type;
}
