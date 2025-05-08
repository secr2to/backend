package com.emelmujiro.secreto.chatting.service.impl;

import com.emelmujiro.secreto.chatting.dto.CreateChattingReqDto;
import com.emelmujiro.secreto.chatting.dto.CreateChattingResDto;
import com.emelmujiro.secreto.chatting.entity.ChattingMessage;
import com.emelmujiro.secreto.chatting.entity.ChattingRoom;
import com.emelmujiro.secreto.chatting.repository.ChattingMessageRepository;
import com.emelmujiro.secreto.chatting.repository.ChattingParticipateRepository;
import com.emelmujiro.secreto.chatting.repository.ChattingRoomRepository;
import com.emelmujiro.secreto.chatting.service.ChattingService;
import com.emelmujiro.secreto.room.entity.RoomUser;
import com.emelmujiro.secreto.room.entity.repository.RoomUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Transactional
@Service
public class ChattingServiceImpl implements ChattingService {

    private final ChattingMessageRepository chattingMessageRepository;
    private final ChattingParticipateRepository chattingParticipateRepository;
    private final ChattingRoomRepository chattingRoomRepository;
    private final RoomUserRepository roomUserRepository;

    @Override
    public CreateChattingResDto createChatting(CreateChattingReqDto createChattingReqDto) {

        RoomUser findRoomUser = roomUserRepository.findById(createChattingReqDto.getWriterId())
                .orElseThrow(() -> new RuntimeException("해당 방 유저가 존재하지 않습니다."));

        ChattingRoom findChattingRoom = chattingRoomRepository.findById(createChattingReqDto.getChattingRoomId())
                .orElseThrow(() -> new RuntimeException("해당 채팅방이 존재하지 않습니다."));

        ChattingMessage newChattingMessage = ChattingMessage.builder()
                .chattingRoom(findChattingRoom)
                .content(createChattingReqDto.getContent())
                .readYn(false)
                .writeDate(LocalDateTime.now())
                .roomUser(findRoomUser)
                .build();

        chattingMessageRepository.save(newChattingMessage);

        return CreateChattingResDto.builder()
                .chattingMessageId(newChattingMessage.getId())
                .build();
    }
}
