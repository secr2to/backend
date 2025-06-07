package com.emelmujiro.secreto.mission.controller;

import com.emelmujiro.secreto.global.response.ApiResponse;
import com.emelmujiro.secreto.mission.dto.request.GetRoomMissionHistoryListRequestDto;
import com.emelmujiro.secreto.mission.dto.request.CreateSystemMissionRequestDto;
import com.emelmujiro.secreto.mission.dto.request.GetRoomMissionListRequestDto;
import com.emelmujiro.secreto.mission.dto.response.CreateSystemMissionResponseDto;
import com.emelmujiro.secreto.mission.dto.response.GetRoomMissionHistoryListResponseDto;
import com.emelmujiro.secreto.mission.dto.response.GetRoomMissionListResponseDto;
import com.emelmujiro.secreto.mission.dto.response.GetSystemMissionListResponseDto;
import com.emelmujiro.secreto.mission.service.MissionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class MissionController {

    private final MissionService missionService;

    /*
    * 기본 시스템 미션 리스트 조회 api
    * */
    @GetMapping("/missions")
    public ResponseEntity<ApiResponse<Object>> getSystemMissionList() {

        List<GetSystemMissionListResponseDto> resultList = missionService.getSystemMissionList();

        return ApiResponse.builder()
                .data(resultList)
                .status(HttpStatus.OK)
                .message("기본 시스템 미션 리스트를 조회하였습니다.")
                .success();
    }

    /*
     * 기본 시스템 미션 생성 api
     * */
    @PostMapping("/missions")
    public ResponseEntity<ApiResponse<Object>> createSystemMission(@RequestBody CreateSystemMissionRequestDto params) {

        CreateSystemMissionResponseDto result = missionService.createSystemMission(params);

        return ApiResponse.builder()
                .data(result)
                .status(HttpStatus.CREATED)
                .message("기본 시스템 미션을 추가하였습니다.")
                .success();
    }

    /*
    * 방 별 미션 리스트 조회 api
    * */
    @GetMapping("/rooms/{roomId}/missions")
    public ResponseEntity<ApiResponse<Object>> getRoomMissionList(@ModelAttribute GetRoomMissionListRequestDto params) {

        List<GetRoomMissionListResponseDto> resultList = missionService.getRoomMissionList(params);

        return ApiResponse.builder()
                .data(resultList)
                .status(HttpStatus.OK)
                .message("방의 미션 리스트를 조회하였습니다.")
                .success();
    }

    /*
     * 방 별 미션 히스토리 리스트 조회 api
     * */
    @GetMapping("/rooms/{roomId}/mission-historys")
    public ResponseEntity<ApiResponse<Object>> getRoomMissionHistoryList(@ModelAttribute GetRoomMissionHistoryListRequestDto params) {

        List<GetRoomMissionHistoryListResponseDto> resultList = missionService.getRoomMissionHistoryList(params);

        return ApiResponse.builder()
                .data(resultList)
                .status(HttpStatus.OK)
                .message("방의 미션 히스토리 리스트를 조회하였습니다.")
                .success();
    }

}
