package com.emelmujiro.secreto.chatting.controller;

import com.emelmujiro.secreto.chatting.dto.CreateChattingReqDto;
import com.emelmujiro.secreto.chatting.dto.CreateChattingResDto;
import com.emelmujiro.secreto.chatting.service.ChattingService;
import com.emelmujiro.secreto.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ChattingController {

    private final ChattingService chattingService;

    @MessageMapping("/chatting/{chattingRoomId}")
    @SendTo("/sub/{chattingRoomId}")    // 소켓 연결
    public ResponseEntity<?> createChatting(@DestinationVariable("chattingRoomId") Long chattingRoomId, @RequestBody CreateChattingReqDto createChattingReqDto) {

        createChattingReqDto.setChattingRoomId(chattingRoomId);

        CreateChattingResDto result = chattingService.createChatting(createChattingReqDto);

        return ApiResponse.builder()
                .data(createChattingReqDto)
                .status(HttpStatus.OK)
                .message("채팅 메시지를 작성하였습니다.")
                .success();
    }
}
