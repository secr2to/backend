package com.emelmujiro.secreto.chatting.service.impl;

import com.emelmujiro.secreto.chatting.dto.*;
import com.emelmujiro.secreto.chatting.entity.ChattingMessage;
import com.emelmujiro.secreto.chatting.entity.ChattingParticipate;
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
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class ChattingServiceImpl implements ChattingService {

    private final ChattingMessageRepository chattingMessageRepository;
    private final ChattingParticipateRepository chattingParticipateRepository;
    private final ChattingRoomRepository chattingRoomRepository;
    private final RoomUserRepository roomUserRepository;

    @Override
    public CreateChattingResDto createChatting(CreateChattingReqDto params) {

        RoomUser findRoomUser = roomUserRepository.findById(params.getWriterId())
                .orElseThrow(() -> new RuntimeException("해당 방 유저가 존재하지 않습니다."));

        ChattingRoom findChattingRoom = chattingRoomRepository.findById(params.getChattingRoomId())
                .orElseThrow(() -> new RuntimeException("해당 채팅방이 존재하지 않습니다."));

        ChattingMessage newChattingMessage = ChattingMessage.builder()
                .chattingRoom(findChattingRoom)
                .content(params.getContent())
                .readYn(false)
                .writeDate(LocalDateTime.now())
                .roomUser(findRoomUser)
                .build();

        chattingMessageRepository.save(newChattingMessage);

        return CreateChattingResDto.builder()
                .chattingMessageId(newChattingMessage.getId())
                .writerId(newChattingMessage.getRoomUser().getId())
                .content(newChattingMessage.getContent())
                .writeDate(newChattingMessage.getWriteDate())
                .readYn(newChattingMessage.getReadYn())
                .chattingRoomId(newChattingMessage.getId())
                .build();
    }

    @Override
    public List<GetChattingListResDto> getChattingList(GetChattingListReqDto params) {

        RoomUser findRoomUser = roomUserRepository.findByUserIdAndRoomId(params.getUserId(), params.getRoomId())
                .orElseThrow(() -> new RuntimeException("해당 유저는 해당 방에 속해있지 않습니다."));

        ChattingParticipate findChattingParticipate = chattingParticipateRepository.findByRoomUserIdAndType(findRoomUser.getId(), params.getType())
                .orElseThrow(() -> new RuntimeException("해당 유저가 참여한 채팅방이 존재하지 않습니다."));

        List<GetChattingListResDto> resultList = chattingMessageRepository.findAllByChattingRoomId(findChattingParticipate.getChattingRoom().getId())
                .stream().map(chattingMessage -> GetChattingListResDto.builder()
                        .chattingRoomId(chattingMessage.getId())
                        .writerId(chattingMessage.getRoomUser().getId())
                        .content(chattingMessage.getContent())
                        .writeDate(chattingMessage.getWriteDate())
                        .readYn(chattingMessage.getReadYn())
                        .build()).toList();

        return resultList;
    }

    @Override
    public List<GetChattingParticipationListResDto> getChattingParticipationList(GetChattingParticipationListReqDto params) {

        RoomUser findRoomUser = roomUserRepository.findByUserIdAndRoomId(params.getUserId(), params.getRoomId())
                .orElseThrow(() -> new RuntimeException("해당 유저는 해당 방에 속해있지 않습니다."));

        List<GetChattingParticipationListResDto> resultList = chattingParticipateRepository.findAllWithChattingRoomByRoomUserId(findRoomUser.getId())
                .stream().map(chattingParticipate -> GetChattingParticipationListResDto.builder()
                        .roomUserId(chattingParticipate.getRoomUser().getId())
                        .chattingRoomId(chattingParticipate.getChattingRoom().getId())
                        .type(chattingParticipate.getChattingUserType())
                        .lastChattingDate(chattingParticipate.getChattingRoom().getLastChattingDate())
                        .build()).toList();

        return resultList;
    }

    @Override
    public List<UpdateChattingReadStatusResDto> updateChattingReadStatus(UpdateChattingReadStatusReqDto params) {

        List<ChattingMessage> chattingMessageList = chattingMessageRepository.findAllByChattingRoomIdAndChattingMessageId(params.getChattingRoomId(), params.getChattingMessageIds());

        List<UpdateChattingReadStatusResDto> resultList = new ArrayList<>();
        for(ChattingMessage cm : chattingMessageList) {

            if(cm.getRoomUser().getId() != params.getRoomUserId()) {
                cm.setMessageRead();
            }

            resultList.add(UpdateChattingReadStatusResDto.builder()
                    .chattingMessageId(cm.getId())
                    .readYn(cm.getReadYn())
                    .build());
        }

        return resultList;
    }
}
