package com.emelmujiro.secreto.room.service.impl;

import com.emelmujiro.secreto.room.dto.request.GetRoomDetailsReqDto;
import com.emelmujiro.secreto.room.dto.request.GetRoomListReqDto;
import com.emelmujiro.secreto.room.dto.request.GetRoomUserListReqDto;
import com.emelmujiro.secreto.room.dto.response.GetRoomDetailsResDto;
import com.emelmujiro.secreto.room.dto.response.GetRoomListResDto;
import com.emelmujiro.secreto.room.dto.response.GetRoomUserListResDto;
import com.emelmujiro.secreto.room.entity.Room;
import com.emelmujiro.secreto.room.entity.RoomUser;
import com.emelmujiro.secreto.room.error.RoomErrorCode;
import com.emelmujiro.secreto.room.exception.RoomException;
import com.emelmujiro.secreto.room.repository.RoomRepository;
import com.emelmujiro.secreto.room.repository.RoomUserRepository;
import com.emelmujiro.secreto.room.service.RoomService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class RoomServiceImpl implements RoomService {

    private final RoomUserRepository roomUserRepository;
    private final RoomRepository roomRepository;

    @Override
    public List<GetRoomListResDto> getRoomList(GetRoomListReqDto params) {

        List<Long> roomIdList = roomUserRepository.findAllByUserId(params.getUserId()).stream()
                .map(roomUser -> roomUser.getRoom().getId())
                .toList();

        List<GetRoomListResDto> resultList = roomRepository.findAllByIdsAndRoomStatus(roomIdList, params.getStatus()).stream()
                .map(room -> GetRoomListResDto.builder()
                                .roomId(room.getId())
                                .name(room.getName())
                                .code(room.getCode())
                                .startDate(room.getStartDate())
                                .endDate(room.getEndDate())
                                .missionPeriod(room.getMissionPeriod())
                                .build())
                .toList();

        return resultList;
    }

    @Override
    public GetRoomDetailsResDto getRoomDetails(GetRoomDetailsReqDto params) {

        Room findRoom = roomUserRepository.findByUserIdAndRoomId(params.getUserId(), params.getRoomId())
                .map(RoomUser::getRoom)
                .orElseThrow(() -> new RoomException(RoomErrorCode.USER_ROOM_INVALID));

        return GetRoomDetailsResDto.from(findRoom);
    }

    @Override
    public List<GetRoomUserListResDto> getRoomUserList(GetRoomUserListReqDto params) {

        RoomUser findRoomUser = roomUserRepository.findByUserIdAndRoomId(params.getUserId(), params.getRoomId())
                .orElseThrow(() -> new RoomException(RoomErrorCode.USER_ROOM_INVALID));

        List<GetRoomUserListResDto> resultList = roomUserRepository.findAllByRoomIdWithRoomCharacterAndRoomProfile(params.getRoomId()).stream()
                .map(roomUser -> GetRoomUserListResDto.builder()
                        .roomUserId(roomUser.getId())
                        .managerYn(roomUser.getManagerYn())
                        .standbyYn(roomUser.getStandbyYn())
                        .nickname(roomUser.getNickname())
                        .useProfileYn(roomUser.getUseProfileYn())
                        .selfIntroduction(roomUser.getSelfIntroduction())
                        .profileUrl(roomUser.getRoomProfile().getUrl())
                        .skinColorRGB(roomUser.getRoomCharacter().getSkinColorRgb())
                        .clothesColorRGB(roomUser.getRoomCharacter().getClothesColorRgb())
                        .build())
                .toList();

        return resultList;
    }
}
