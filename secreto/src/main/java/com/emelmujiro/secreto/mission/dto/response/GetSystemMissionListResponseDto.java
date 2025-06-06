package com.emelmujiro.secreto.mission.dto.response;

import lombok.*;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class GetSystemMissionListResponseDto {

    private Long systemMissionId;
    private String content;
}
