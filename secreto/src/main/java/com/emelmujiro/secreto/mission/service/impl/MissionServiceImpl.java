package com.emelmujiro.secreto.mission.service.impl;

import com.emelmujiro.secreto.mission.dto.response.GetSystemMissionListResponseDto;
import com.emelmujiro.secreto.mission.repository.SystemMissionRepository;
import com.emelmujiro.secreto.mission.service.MissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class MissionServiceImpl implements MissionService {

    private final SystemMissionRepository systemMissionRepository;

    @Override
    public List<GetSystemMissionListResponseDto> getSystemMissionList() {

        return systemMissionRepository.findAll().stream()
                .map(systemMission -> GetSystemMissionListResponseDto.builder()
                        .systemMissionId(systemMission.getId())
                        .content(systemMission.getContent())
                        .build()).toList();
    }
}
