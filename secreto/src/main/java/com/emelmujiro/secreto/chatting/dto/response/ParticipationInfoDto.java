package com.emelmujiro.secreto.chatting.dto.response;

import lombok.*;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ParticipationInfoDto {

    private Long roomUserId;
    private String nickname;
}
