package com.emelmujiro.secreto.mission.service;

import com.emelmujiro.secreto.mission.dto.request.CreateSystemMissionRequestDto;
import com.emelmujiro.secreto.mission.dto.request.GetRoomMissionListRequestDto;
import com.emelmujiro.secreto.mission.dto.response.CreateSystemMissionResponseDto;
import com.emelmujiro.secreto.mission.dto.response.GetRoomMissionListResponseDto;
import com.emelmujiro.secreto.mission.dto.response.GetSystemMissionListResponseDto;

import java.util.List;

public interface MissionService {

    List<GetSystemMissionListResponseDto> getSystemMissionList();

    CreateSystemMissionResponseDto createSystemMission(CreateSystemMissionRequestDto params);

    List<GetRoomMissionListResponseDto> getRoomMissionList(GetRoomMissionListRequestDto params);
}
