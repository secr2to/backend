package com.emelmujiro.secreto.chatting.controller;

import com.emelmujiro.secreto.auth.annotation.LoginUser;
import com.emelmujiro.secreto.chatting.dto.request.CreateChattingReqDto;
import com.emelmujiro.secreto.chatting.dto.request.GetChattingParticipationListReqDto;
import com.emelmujiro.secreto.chatting.dto.request.UpdateChattingReadStatusReqDto;
import com.emelmujiro.secreto.chatting.dto.response.CreateChattingResDto;
import com.emelmujiro.secreto.chatting.dto.request.GetChattingListReqDto;
import com.emelmujiro.secreto.chatting.dto.response.GetChattingListResDto;
import com.emelmujiro.secreto.chatting.dto.response.GetChattingParticipationListResDto;
import com.emelmujiro.secreto.chatting.dto.response.UpdateChattingReadStatusResDto;
import com.emelmujiro.secreto.chatting.service.ChattingService;
import com.emelmujiro.secreto.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ChattingController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChattingService chattingService;

    /*
    * 채팅 작성 api
    * */
    @PostMapping("/chattings/{chattingRoomId}/messages")
    public ResponseEntity<?> createChatting(@PathVariable("chattingRoomId") Long chattingRoomId, @RequestBody CreateChattingReqDto params) {

        params.setChattingRoomId(chattingRoomId);

        CreateChattingResDto result = chattingService.createChatting(params);

        messagingTemplate.convertAndSend("/sub/" + chattingRoomId, result);

        return ApiResponse.builder()
                .data(result)
                .status(HttpStatus.OK)
                .message("채팅 메시지를 작성하였습니다.")
                .success();
    }

    /*
    * 채팅 내역 조회 api
    * */
    @GetMapping("/rooms/{roomId}/chattings/{type}/messages")
    public ResponseEntity<?> getChattingList(@PathVariable("roomId") Long roomId, @PathVariable("type") String type, @LoginUser Long userId) {

        GetChattingListReqDto params = GetChattingListReqDto.builder()
                .roomId(roomId)
                .userId(userId)
                .type(type)
                .build();

        List<GetChattingListResDto> resultList = chattingService.getChattingList(params);

        return ApiResponse.builder()
                .data(resultList)
                .status(HttpStatus.OK)
                .message("채팅 내역을 조회하였습니다.")
                .success();

    }

    /*
    * 채팅 참여 정보 리스트 조회 api
    * */
    @GetMapping("/rooms/{roomId}/chattings/participation")
    public ResponseEntity<?> getChattingParticipationList(@PathVariable("roomId") Long roomId, @LoginUser Long userId) {

        GetChattingParticipationListReqDto params = GetChattingParticipationListReqDto.builder()
                .roomId(roomId)
                .userId(userId)
                .build();

        List<GetChattingParticipationListResDto> resultList = chattingService.getChattingParticipationList(params);

        return ApiResponse.builder()
                .data(resultList)
                .status(HttpStatus.OK)
                .message("채팅 참여 리스트를 조회하였습니다.")
                .success();
    }

    /*
    * 채팅 읽음 처리 api
    * */
    @PostMapping("/chattings/{chattingRoomId}/messages/read")
    public ResponseEntity<?> updateChattingReadStatus(@PathVariable("chattingRoomId") Long chattingRoomId, @RequestBody UpdateChattingReadStatusReqDto params) {

        params.setChattingRoomId(chattingRoomId);

        List<UpdateChattingReadStatusResDto> resultList = chattingService.updateChattingReadStatus(params);

        return ApiResponse.builder()
                .data(resultList)
                .status(HttpStatus.OK)
                .message("채팅 메시지를 읽음 처리하였습니다.")
                .success();
    }

}
