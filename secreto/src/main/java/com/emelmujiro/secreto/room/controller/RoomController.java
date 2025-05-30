package com.emelmujiro.secreto.room.controller;

import com.emelmujiro.secreto.auth.annotation.LoginUser;
import com.emelmujiro.secreto.global.response.ApiResponse;
import com.emelmujiro.secreto.room.dto.request.*;
import com.emelmujiro.secreto.room.dto.response.*;
import com.emelmujiro.secreto.room.entity.RoomStatus;
import com.emelmujiro.secreto.room.error.RoomErrorCode;
import com.emelmujiro.secreto.room.exception.RoomException;
import com.emelmujiro.secreto.room.service.RoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/rooms")
@RequiredArgsConstructor
@RestController
public class RoomController {

    private final RoomService roomService;

    /*
    * 방 목록 조회 api
    * */
    @GetMapping("")
    public ResponseEntity<ApiResponse<Object>> getRoomList(@RequestParam("status") String status, @LoginUser Long userId) {

        try {
            RoomStatus roomStatus = RoomStatus.fromString(status);

            GetRoomListRequestDto params = GetRoomListRequestDto.builder()
                    .status(roomStatus)
                    .userId(userId)
                    .build();

            List<GetRoomListResponseDto> resultList = roomService.getRoomList(params);

            return ApiResponse.builder()
                    .data(resultList)
                    .status(HttpStatus.OK)
                    .message("방 목록을 조회하였습니다.")
                    .success();

        } catch (IllegalArgumentException e) {

            throw new RoomException(RoomErrorCode.INVALID_ROOM_STATUS);
        }
    }

    /*
    * 방 조회 api
    * */
    @GetMapping("/{roomId}")
    public ResponseEntity<ApiResponse<Object>> getRoomDetails(@PathVariable("roomId") Long roomId, @LoginUser Long userId) {

        GetRoomDetailsRequestDto params = GetRoomDetailsRequestDto.builder()
                .roomId(roomId)
                .userId(userId)
                .build();

        GetRoomDetailsResponseDto result = roomService.getRoomDetails(params);

        return ApiResponse.builder()
                .data(result)
                .status(HttpStatus.OK)
                .message("방을 조회하였습니다.")
                .success();
    }

    /*
    * 방 유저 목록 조회 api
    * */
    @GetMapping("/{roomId}/users")
    public ResponseEntity<ApiResponse<Object>> getRoomUserList(@PathVariable("roomId") Long roomId, @LoginUser Long userId) {

        GetRoomUserListRequestDto params = GetRoomUserListRequestDto.builder()
                .roomId(roomId)
                .userId(userId)
                .build();

        List<GetRoomUserListResponseDto> resultList = roomService.getRoomUserList(params);

        return ApiResponse.builder()
                .data(resultList)
                .status(HttpStatus.OK)
                .message("방 유저 목록을 조회하였습니다.")
                .success();
    }

    /*
    * 방 유저 조회 api
    * */
    @GetMapping("/{roomId}/users/{roomUserId}")
    public ResponseEntity<ApiResponse<Object>> getRoomUserDetails(@PathVariable("roomId") Long roomId, @PathVariable("roomUserId") Long roomUserId, @LoginUser Long userId) {

        GetRoomUserDetailsRequestDto params = GetRoomUserDetailsRequestDto.builder()
                .roomId(roomId)
                .roomUserId(roomUserId)
                .userId(userId)
                .build();

        GetRoomUserDetailsResponseDto result = roomService.getRoomUserDetails(params);

        return ApiResponse.builder()
                .data(result)
                .status(HttpStatus.OK)
                .message("방 유저 세부 정보를 조회하였습니다.")
                .success();
    }

    /*
    * 방 생성 api
    * */
    @PostMapping("")
    public ResponseEntity<ApiResponse<Object>> createRoom(@ModelAttribute CreateRoomRequestDto params) {

        System.out.println("params : " + params);

        CreateRoomResponseDto result = roomService.createRoom(params);

        return ApiResponse.builder()
                .data(result)
                .status(HttpStatus.CREATED)
                .message("방을 생성하였습니다.")
                .success();
    }

    /*
    * 방 정보 변경 api
    * */
    @PutMapping("/{roomId}/details")
    public ResponseEntity<ApiResponse<Object>> updateRoomDetails(@RequestBody UpdateRoomDetailsRequestDto params) {

        UpdateRoomDetailsResponseDto result = roomService.updateRoomDetails(params);

        return ApiResponse.builder()
                .data(result)
                .status(HttpStatus.OK)
                .message("방 정보를 변경하였습니다.")
                .success();
    }

    /*
    * 방 시작 api
    * */
    @PutMapping("/{roomId}/start")
    public ResponseEntity<ApiResponse<Object>> updateRoomStatusStart(@RequestBody UpdateRoomStatusStartRequestDto params) {

        UpdateRoomStatusStartResponseDto result = roomService.updateRoomStatusStart(params);

        return ApiResponse.builder()
                .data(result)
                .status(HttpStatus.OK)
                .message("방을 시작하였습니다.")
                .success();
    }

    /*
    * 방 종료 api
    * */
    @PutMapping("/{roomId}/end")
    public ResponseEntity<ApiResponse<Object>> updateRoomStatusEnd(@PathVariable("roomId") Long roomId, @LoginUser Long userId) {

        UpdateRoomStatusEndRequestDto params = UpdateRoomStatusEndRequestDto.builder()
                .roomId(roomId)
                .userId(userId)
                .build();

        roomService.updateRoomStatusEnd(params);

        return ApiResponse.builder()
                .data(null)
                .status(HttpStatus.OK)
                .message("방을 종료하였습니다.")
                .success();
    }

    /*
    * 방 코드 입력 및 조회
    * */
    @PostMapping("/code")
    public ResponseEntity<ApiResponse<Object>> enterRoomByCode(@RequestBody EnterRoomByCodeRequestDto params) {

        EnterRoomByCodeResponseDto result = roomService.enterRoomByCode(params);

        return ApiResponse.builder()
                .data(result)
                .status(HttpStatus.OK)
                .message("방 코드를 통해 방을 조회하였습니다.")
                .success();
    }

    /*
    * 인게임 프로필 정보 생성 api
    * */
    @PostMapping("/{roomId}/profile")
    public ResponseEntity<ApiResponse<Object>> createRoomUserProfile(@ModelAttribute CreateRoomUserProfileRequestDto params) {

        CreateRoomUserProfileResponseDto result = roomService.createRoomUserProfile(params);

        return ApiResponse.builder()
                .data(result)
                .status(HttpStatus.OK)
                .message("인게임 프로필 정보를 생성하였습니다.")
                .success();
    }

    /*
    * 인게임 프로필 자기소개 수정 api
    * */
    @PutMapping("/{roomId}/profile/{roomUserId}")
    public ResponseEntity<ApiResponse<Object>> updateRoomUserSelfIntroduction(@RequestBody UpdateRoomUserSelfIntroductionRequestDto params) {

        UpdateRoomUserSelfIntroductionResponseDto result = roomService.updateRoomUserSelfIntroduction(params);

        return ApiResponse.builder()
                .data(result)
                .status(HttpStatus.OK)
                .message("자기소개를 수정하였습니다.")
                .success();
    }

}















