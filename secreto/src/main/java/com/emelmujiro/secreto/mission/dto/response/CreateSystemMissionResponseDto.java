package com.emelmujiro.secreto.mission.dto.response;

import com.emelmujiro.secreto.mission.entity.SystemMission;
import lombok.*;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CreateSystemMissionResponseDto {

    private Long systemMissionId;

    public static CreateSystemMissionResponseDto from(SystemMission systemMission) {

        return CreateSystemMissionResponseDto.builder()
                .systemMissionId(systemMission.getId())
                .build();
    }
}
