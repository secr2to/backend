package com.emelmujiro.secreto.chatting.dto.response;

import com.emelmujiro.secreto.chatting.entity.ChattingParticipate;
import com.emelmujiro.secreto.chatting.entity.ChattingParticipateType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class GetChattingRoomDetailsResponseDto {

    private Long chattingRoomId;
    private ChattingParticipateType chattingRoomType;
    private LocalDateTime lastChattingDate;

    private List<ParticipationInfoDto> participationInfoList;

}
