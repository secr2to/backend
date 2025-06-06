package com.emelmujiro.secreto.mission.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class GetRoomMissionHistoryListResponseDto {

    private Long roomId;
    private String content;
    private LocalDateTime createDate;
    private Boolean completeYn;
}
