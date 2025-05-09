package com.emelmujiro.secreto.chatting.controller;

import com.emelmujiro.secreto.auth.annotation.LoginUser;
import com.emelmujiro.secreto.chatting.dto.CreateChattingReqDto;
import com.emelmujiro.secreto.chatting.dto.CreateChattingResDto;
import com.emelmujiro.secreto.chatting.dto.GetChattingListReqDto;
import com.emelmujiro.secreto.chatting.dto.GetChattingListResDto;
import com.emelmujiro.secreto.chatting.service.ChattingService;
import com.emelmujiro.secreto.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class ChattingController {

    private final SimpMessagingTemplate messagingTemplate;
    private final ChattingService chattingService;

    @PostMapping("/chatting/{chattingRoomId}")
    public ResponseEntity<?> createChatting(@PathVariable("chattingRoomId") Long chattingRoomId, @RequestBody CreateChattingReqDto createChattingReqDto) {

        createChattingReqDto.setChattingRoomId(chattingRoomId);

        CreateChattingResDto result = chattingService.createChatting(createChattingReqDto);

        messagingTemplate.convertAndSend("/sub/" + chattingRoomId, createChattingReqDto);

        return ApiResponse.builder()
                .data(result)
                .status(HttpStatus.OK)
                .message("채팅 메시지를 작성하였습니다.")
                .success();
    }

    @GetMapping("/chatting/{roomId}")
    public ResponseEntity<?> getChattingList(@PathVariable("roomId") Long roomId, @RequestParam("type") String type, @LoginUser Long userId) {

        GetChattingListReqDto getChattingListReqDto = GetChattingListReqDto.builder()
                .roomId(roomId)
                .userId(userId)
                .type(type)
                .build();

        List<GetChattingListResDto> resultList = chattingService.getChattingList(getChattingListReqDto);

        return ApiResponse.builder()
                .data(resultList)
                .status(HttpStatus.OK)
                .message("채팅 메시지를 작성하였습니다.")
                .success();

    }

}
