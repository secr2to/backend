package com.emelmujiro.secreto.room.controller;

import com.emelmujiro.secreto.auth.annotation.LoginUser;
import com.emelmujiro.secreto.global.response.ApiResponse;
import com.emelmujiro.secreto.room.dto.request.GetRoomListReqDto;
import com.emelmujiro.secreto.room.dto.response.GetRoomListResDto;
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
    public ResponseEntity<?> getRoomList(@RequestParam("status") String status, @LoginUser Long userId) {

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
}
