package com.emelmujiro.secreto.chatting.service.impl;

import com.emelmujiro.secreto.chatting.dto.request.CreateChattingRequestDto;
import com.emelmujiro.secreto.chatting.dto.request.GetChattingParticipationListRequestDto;
import com.emelmujiro.secreto.chatting.dto.request.UpdateChattingReadStatusRequestDto;
import com.emelmujiro.secreto.chatting.dto.response.CreateChattingResponseDto;
import com.emelmujiro.secreto.chatting.dto.request.GetChattingListRequestDto;
import com.emelmujiro.secreto.chatting.dto.response.GetChattingListResponseDto;
import com.emelmujiro.secreto.chatting.dto.response.GetChattingParticipationListResponseDto;
import com.emelmujiro.secreto.chatting.dto.response.UpdateChattingReadStatusResponseDto;
import com.emelmujiro.secreto.chatting.entity.ChattingMessage;
import com.emelmujiro.secreto.chatting.entity.ChattingParticipate;
import com.emelmujiro.secreto.chatting.entity.ChattingRoom;
import com.emelmujiro.secreto.chatting.error.ChattingErrorCode;
import com.emelmujiro.secreto.chatting.exception.ChattingException;
import com.emelmujiro.secreto.chatting.repository.ChattingMessageRepository;
import com.emelmujiro.secreto.chatting.repository.ChattingParticipateRepository;
import com.emelmujiro.secreto.chatting.repository.ChattingRoomRepository;
import com.emelmujiro.secreto.chatting.service.ChattingService;
import com.emelmujiro.secreto.room.entity.RoomUser;
import com.emelmujiro.secreto.room.repository.RoomUserRepository;
import com.emelmujiro.secreto.room.error.RoomErrorCode;
import com.emelmujiro.secreto.room.exception.RoomException;
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
    public CreateChattingResponseDto createChatting(CreateChattingRequestDto params) {

        RoomUser findRoomUser = roomUserRepository.findById(params.getWriterId())
                .orElseThrow(() -> new RoomException(RoomErrorCode.NOT_EXIST_ROOM_USER));

        ChattingRoom findChattingRoom = chattingRoomRepository.findById(params.getChattingRoomId())
                .orElseThrow(() -> new ChattingException(ChattingErrorCode.NOT_EXIST_CHATTING_ROOM));

        ChattingMessage newChattingMessage = ChattingMessage.builder()
                .chattingRoom(findChattingRoom)
                .content(params.getContent())
                .readYn(false)
                .writeDate(LocalDateTime.now())
                .roomUser(findRoomUser)
                .build();

        chattingMessageRepository.save(newChattingMessage);

        return CreateChattingResponseDto.builder()
                .chattingMessageId(newChattingMessage.getId())
                .writerId(newChattingMessage.getRoomUser().getId())
                .content(newChattingMessage.getContent())
                .writeDate(newChattingMessage.getWriteDate())
                .readYn(newChattingMessage.getReadYn())
                .chattingRoomId(newChattingMessage.getId())
                .build();
    }

    @Override
    public List<GetChattingListResponseDto> getChattingList(GetChattingListRequestDto params) {

        RoomUser findRoomUser = roomUserRepository.findByUserIdAndRoomId(params.getUserId(), params.getRoomId())
                .orElseThrow(() -> new RoomException(RoomErrorCode.USER_ROOM_INVALID));

        ChattingParticipate findChattingParticipate = chattingParticipateRepository.findByRoomUserIdAndType(findRoomUser.getId(), params.getType())
                .orElseThrow(() -> new ChattingException(ChattingErrorCode.NOT_EXIST_CHATTING_ROOM));

        List<GetChattingListResponseDto> resultList = chattingMessageRepository.findAllByChattingRoomId(findChattingParticipate.getChattingRoom().getId())
                .stream().map(chattingMessage -> GetChattingListResponseDto.builder()
                        .chattingRoomId(chattingMessage.getId())
                        .writerId(chattingMessage.getRoomUser().getId())
                        .content(chattingMessage.getContent())
                        .writeDate(chattingMessage.getWriteDate())
                        .readYn(chattingMessage.getReadYn())
                        .build()).toList();

        return resultList;
    }

    @Override
    public List<GetChattingParticipationListResponseDto> getChattingParticipationList(GetChattingParticipationListRequestDto params) {

        RoomUser findRoomUser = roomUserRepository.findByUserIdAndRoomId(params.getUserId(), params.getRoomId())
                .orElseThrow(() -> new RoomException(RoomErrorCode.USER_ROOM_INVALID));

        List<GetChattingParticipationListResponseDto> resultList = chattingParticipateRepository.findAllWithChattingRoomByRoomUserId(findRoomUser.getId())
                .stream().map(chattingParticipate -> GetChattingParticipationListResponseDto.builder()
                        .roomUserId(chattingParticipate.getRoomUser().getId())
                        .chattingRoomId(chattingParticipate.getChattingRoom().getId())
                        .type(chattingParticipate.getChattingUserType())
                        .lastChattingDate(chattingParticipate.getChattingRoom().getLastChattingDate())
                        .build()).toList();

        return resultList;
    }

    @Override
    public List<UpdateChattingReadStatusResponseDto> updateChattingReadStatus(UpdateChattingReadStatusRequestDto params) {

        List<ChattingMessage> chattingMessageList = chattingMessageRepository.findAllByChattingRoomIdAndChattingMessageId(params.getChattingRoomId(), params.getChattingMessageIds());

        List<UpdateChattingReadStatusResponseDto> resultList = new ArrayList<>();
        for(ChattingMessage cm : chattingMessageList) {

            if(cm.getRoomUser().getId() != params.getRoomUserId()) {
                cm.setMessageRead();
            }

            resultList.add(UpdateChattingReadStatusResponseDto.builder()
                    .chattingMessageId(cm.getId())
                    .readYn(cm.getReadYn())
                    .build());
        }

        return resultList;
    }
}
