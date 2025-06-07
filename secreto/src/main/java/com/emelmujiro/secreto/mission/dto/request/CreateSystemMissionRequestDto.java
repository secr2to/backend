package com.emelmujiro.secreto.mission.dto.request;

import lombok.*;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CreateSystemMissionRequestDto {

    private String content;
}
