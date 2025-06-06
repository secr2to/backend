package com.emelmujiro.secreto.mission.service.impl;

import com.emelmujiro.secreto.mission.dto.request.CreateSystemMissionRequestDto;
import com.emelmujiro.secreto.mission.dto.request.GetRoomMissionListRequestDto;
import com.emelmujiro.secreto.mission.dto.response.CreateSystemMissionResponseDto;
import com.emelmujiro.secreto.mission.dto.response.GetRoomMissionListResponseDto;
import com.emelmujiro.secreto.mission.dto.response.GetSystemMissionListResponseDto;
import com.emelmujiro.secreto.mission.entity.SystemMission;
import com.emelmujiro.secreto.mission.repository.SystemMissionRepository;
import com.emelmujiro.secreto.mission.service.MissionService;
import com.emelmujiro.secreto.room.repository.RoomMissionRepository;
import com.emelmujiro.secreto.room.repository.RoomUserRepository;
import com.emelmujiro.secreto.room.service.impl.RoomAuthorizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class MissionServiceImpl implements MissionService {

    private final SystemMissionRepository systemMissionRepository;
    private final RoomUserRepository roomUserRepository;
    private final RoomAuthorizationService roomAuthorizationService;
    private final RoomMissionRepository roomMissionRepository;

    @Override
    public List<GetSystemMissionListResponseDto> getSystemMissionList() {

        return systemMissionRepository.findAll().stream()
                .map(systemMission -> GetSystemMissionListResponseDto.builder()
                        .systemMissionId(systemMission.getId())
                        .content(systemMission.getContent())
                        .build()).toList();
    }

    @Override
    public CreateSystemMissionResponseDto createSystemMission(CreateSystemMissionRequestDto params) {

        SystemMission newSystemMission = SystemMission.builder()
                .content(params.getContent())
                .build();

        SystemMission findSystemMission = systemMissionRepository.save(newSystemMission);

        return CreateSystemMissionResponseDto.from(findSystemMission);
    }

    @Override
    public List<GetRoomMissionListResponseDto> getRoomMissionList(GetRoomMissionListRequestDto params) {

        // 방에 소속된 유저인지 확인
        roomAuthorizationService.checkIsRoomUser(params.getUserId(), params.getRoomId());

        return roomMissionRepository.findAll().stream()
                .map(roomMission -> GetRoomMissionListResponseDto.builder()
                        .roomId(roomMission.getRoom().getId())
                        .content(roomMission.getContent())
                        .executeYn(roomMission.getExecuteYn())
                        .build()).toList();
    }
}
