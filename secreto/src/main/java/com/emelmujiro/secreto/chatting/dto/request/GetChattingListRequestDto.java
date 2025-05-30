package com.emelmujiro.secreto.chatting.dto.request;

import lombok.*;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class GetChattingListRequestDto {

    private Long roomId;

    private Long userId;

    private String type;
}
