package com.emelmujiro.secreto.mission.controller;

import com.emelmujiro.secreto.global.response.ApiResponse;
import com.emelmujiro.secreto.mission.dto.response.GetSystemMissionListResponseDto;
import com.emelmujiro.secreto.mission.service.MissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/missions")
@RequiredArgsConstructor
@RestController
public class MissionController {

    private final MissionService missionService;

    /*
    * 기본 시스템 미션 리스트 조회 api
    * */
    @GetMapping("")
    public ResponseEntity<ApiResponse<Object>> getSystemMissionList() {

        List<GetSystemMissionListResponseDto> resultList = missionService.getSystemMissionList();

        return ApiResponse.builder()
                .data(resultList)
                .status(HttpStatus.OK)
                .message("기본 시스템 미션 리스트를 조회하였습니다.")
                .success();
    }


}
