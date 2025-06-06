package com.emelmujiro.secreto.mission.service;

import com.emelmujiro.secreto.mission.dto.response.GetSystemMissionListResponseDto;

import java.util.List;

public interface MissionService {

    List<GetSystemMissionListResponseDto> getSystemMissionList();
}
