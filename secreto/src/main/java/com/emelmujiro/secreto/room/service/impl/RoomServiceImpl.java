package com.emelmujiro.secreto.room.service.impl;

import com.emelmujiro.secreto.mission.entity.RoomMission;
import com.emelmujiro.secreto.room.dto.request.*;
import com.emelmujiro.secreto.room.dto.response.*;
import com.emelmujiro.secreto.room.entity.Room;
import com.emelmujiro.secreto.room.entity.RoomUser;
import com.emelmujiro.secreto.room.error.RoomErrorCode;
import com.emelmujiro.secreto.room.exception.RoomException;
import com.emelmujiro.secreto.room.repository.RoomMissionRepository;
import com.emelmujiro.secreto.room.repository.RoomRepository;
import com.emelmujiro.secreto.room.repository.RoomUserRepository;
import com.emelmujiro.secreto.room.service.RoomService;
import com.emelmujiro.secreto.room.utils.GenerateRandomCodeUtil;
import com.emelmujiro.secreto.user.entity.User;
import com.emelmujiro.secreto.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@RequiredArgsConstructor
@Transactional
@Service
public class RoomServiceImpl implements RoomService {

    private final RoomUserRepository roomUserRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final RoomMissionRepository roomMissionRepository;

    private final RoomAuthorizationService roomAuthorizationService;

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

        // 방에 소속된 유저인지 확인
        roomAuthorizationService.checkIsRoomUser(params.getUserId(), params.getRoomId());

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

    @Override
    public GetRoomUserDetailsResDto getRoomUserDetails(GetRoomUserDetailsReqDto params) {

        // 방에 소속된 유저인지 확인
        roomAuthorizationService.checkIsRoomUser(params.getUserId(), params.getRoomId());

        RoomUser findRoomUser = roomUserRepository.findByIdAndRoomIdWithRoomCharacterAndRoomProfile(params.getRoomUserId(), params.getRoomId())
                .orElseThrow(() -> new RoomException(RoomErrorCode.ROOMUSER_ROOM_INVALID));

        return GetRoomUserDetailsResDto.from(findRoomUser);
    }

    @Override
    public CreateRoomResDto createRoom(CreateRoomReqDto params) {

        String generatedcode = "";
        boolean isCodeExists = true;
        while(isCodeExists) {

            generatedcode = GenerateRandomCodeUtil.generateRandomCode();

            if(roomRepository.findByCode(generatedcode).isEmpty()) {
                isCodeExists = false;
            }
        }

        Room newRoom = Room.builder()
                .name(params.getName())
                .endDate(params.getEndDate())
                .missionPeriod(params.getMissionPeriod())
                .code(generatedcode)
                .build();

        User findUser = userRepository.findById(params.getManagerId()).orElseThrow(() -> new RuntimeException("해당 유저가 존재하지 않습니다."));
        newRoom.addRoomUser(findUser);

        roomRepository.save(newRoom);

        return CreateRoomResDto.from(newRoom);
    }

    @Override
    public UpdateRoomDetailsResDto updateRoomDetails(UpdateRoomDetailsReqDto params) {

        // 방장인지 권한 확인
        RoomUser findRoomUser = roomAuthorizationService.checkIsManager(params.getUserId(), params.getRoomId());

        // 방 정보 수정
        Room findRoom = roomRepository.findById(findRoomUser.getRoom().getId())
                .orElseThrow(() -> new RoomException(RoomErrorCode.NOT_EXIST_ROOM));

        findRoom.updateRoomInfo(params.getEndDate(), params.getMissionPeriod());

        return UpdateRoomDetailsResDto.from(findRoom);
    }

    @Override
    public UpdateRoomStatusStartResDto updateRoomStatusStart(UpdateRoomStatusStartReqDto params) {

        // 방장인지 권한 확인
        roomAuthorizationService.checkIsManager(params.getUserId(), params.getRoomId());

        // 대기상태인 방 유저들 삭제
        List<RoomUser> findRoomUserNotAcceptedList = roomUserRepository.findAllByRoomIdAndStandbyYnFalse(params.getRoomId());
        roomUserRepository.deleteAll(findRoomUserNotAcceptedList);

        // 방 시작
        Room findRoom = roomRepository.findById(params.getRoomId())
                .orElseThrow(() -> new RoomException(RoomErrorCode.NOT_EXIST_ROOM));

        findRoom.startRoom();

        // 미션 리스트 생성
        List<RoomMission> newRoomMissionList = params.getMissionList().stream()
                .map(content -> RoomMission.builder()
                        .room(findRoom)
                        .content(content)
                        .executeYn(false)
                        .build())
                .toList();

        roomMissionRepository.saveAll(newRoomMissionList);

        // TODO: 남아있는 방 유저들간 마니또, 마니띠 관계 매칭

        // TODO: 방 유저들 간 채팅 생성

        return UpdateRoomStatusStartResDto.from(findRoom);
    }

    @Override
    public void updateRoomStatusEnd(UpdateRoomStatusEndReqDto params) {

        // 방장인지 권한 확인
        roomAuthorizationService.checkIsManager(params.getUserId(), params.getRoomId());

        Room findRoom = roomRepository.findById(params.getRoomId())
                .orElseThrow(() -> new RoomException(RoomErrorCode.NOT_EXIST_ROOM));

        findRoom.terminateRoom();

        // TODO: 방 히스토리 저장, 필요없는 잔여 데이터 삭제

    }
}
