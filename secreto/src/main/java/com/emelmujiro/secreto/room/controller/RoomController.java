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
import java.util.Objects;

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

            GetRoomListReqDto params = GetRoomListReqDto.builder()
                    .status(roomStatus)
                    .userId(userId)
                    .build();

            List<GetRoomListResDto> resultList = roomService.getRoomList(params);

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

        GetRoomDetailsReqDto params = GetRoomDetailsReqDto.builder()
                .roomId(roomId)
                .userId(userId)
                .build();

        GetRoomDetailsResDto result = roomService.getRoomDetails(params);

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

        GetRoomUserListReqDto params = GetRoomUserListReqDto.builder()
                .roomId(roomId)
                .userId(userId)
                .build();

        List<GetRoomUserListResDto> resultList = roomService.getRoomUserList(params);

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

        GetRoomUserDetailsReqDto params = GetRoomUserDetailsReqDto.builder()
                .roomId(roomId)
                .roomUserId(roomUserId)
                .userId(userId)
                .build();

        GetRoomUserDetailsResDto result = roomService.getRoomUserDetails(params);

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
    public ResponseEntity<ApiResponse<Object>> createRoom(@RequestBody CreateRoomReqDto params, @LoginUser Long userId) {

        params.setManagerId(userId);

        CreateRoomResDto result = roomService.createRoom(params);

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
    public ResponseEntity<ApiResponse<Object>> updateRoomDetails(@PathVariable("roomId") Long roomId, @RequestBody UpdateRoomDetailsReqDto params, @LoginUser Long userId) {

        params.setRoomId(roomId);
        params.setUserId(userId);

        UpdateRoomDetailsResDto result = roomService.updateRoomDetails(params);

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
    public ResponseEntity<ApiResponse<Object>> updateRoomStatusStart(@PathVariable("roomId") Long roomId, @RequestBody UpdateRoomStatusStartReqDto params, @LoginUser Long userId) {

        params.setRoomId(roomId);
        params.setUserId(userId);

        UpdateRoomStatusStartResDto result = roomService.updateRoomStatusStart(params);

        return ApiResponse.builder()
                .data(result)
                .status(HttpStatus.OK)
                .message("방을 시작하였습니다.")
                .success();
    }


}















