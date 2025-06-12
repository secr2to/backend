package com.emelmujiro.secreto.room.service.impl;

import com.emelmujiro.secreto.chatting.entity.ChattingParticipate;
import com.emelmujiro.secreto.chatting.entity.ChattingParticipateType;
import com.emelmujiro.secreto.chatting.entity.ChattingRoom;
import com.emelmujiro.secreto.chatting.repository.ChattingParticipateRepository;
import com.emelmujiro.secreto.chatting.repository.ChattingRoomRepository;
import com.emelmujiro.secreto.game.entity.Matching;
import com.emelmujiro.secreto.game.error.GameErrorCode;
import com.emelmujiro.secreto.game.exception.GameException;
import com.emelmujiro.secreto.game.repository.MatchingRepository;
import com.emelmujiro.secreto.game.repository.SystemCharacterColorRepository;
import com.emelmujiro.secreto.global.service.S3DirectoryName;
import com.emelmujiro.secreto.global.service.S3Service;
import com.emelmujiro.secreto.mission.entity.RoomMission;
import com.emelmujiro.secreto.room.dto.request.*;
import com.emelmujiro.secreto.room.dto.response.*;
import com.emelmujiro.secreto.room.entity.*;
import com.emelmujiro.secreto.room.error.RoomErrorCode;
import com.emelmujiro.secreto.room.exception.RoomException;
import com.emelmujiro.secreto.room.repository.RoomMissionRepository;
import com.emelmujiro.secreto.room.repository.RoomRepository;
import com.emelmujiro.secreto.room.repository.RoomUserRepository;
import com.emelmujiro.secreto.room.service.RoomService;
import com.emelmujiro.secreto.room.util.GenerateRandomCodeUtil;
import com.emelmujiro.secreto.user.entity.User;
import com.emelmujiro.secreto.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class RoomServiceImpl implements RoomService {

    @Value("${s3.access-minute}")
    private int accessMinute;

    private final RoomUserRepository roomUserRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final RoomMissionRepository roomMissionRepository;
    private final SystemCharacterColorRepository systemCharacterColorRepository;
    private final MatchingRepository matchingRepository;

    private final RoomAuthorizationService roomAuthorizationService;
    private final ChattingRoomRepository chattingRoomRepository;
    private final ChattingParticipateRepository chattingParticipateRepository;

    private final S3Service s3Service;

    @Transactional(readOnly = true)
    @Override
    public List<GetRoomListResponseDto> getRoomList(GetRoomListRequestDto params) {

        List<Long> roomIdList = roomUserRepository.findAllByUserId(params.getUserId()).stream()
                .map(roomUser -> roomUser.getRoom().getId())
                .toList();

        List<Room> rooms;
        if (params.getStatus() == null) {
            rooms = roomRepository.findAllByIds(roomIdList);
        } else {
            rooms = roomRepository.findAllByIdsAndRoomStatus(roomIdList, params.getStatus());
        }

        List<GetRoomListResponseDto> resultList = rooms.stream()
                .map(room -> {
                    int roomUserCount = roomUserRepository.countByRoomIdAndStandbyYn(room.getId(), false);
                    RoomUser ownerUser = roomUserRepository.findByRoomIdAndManagerYn(room.getId(), true);

                    return GetRoomListResponseDto.builder()
                            .roomId(room.getId())
                            .name(room.getName())
                            .code(room.getCode())
                            .startDate(room.getStartDate())
                            .endDate(room.getEndDate())
                            .missionPeriod(room.getMissionPeriod())
                            .status(room.getRoomStatus())
                            .imageUrl(room.getImageUrl())
                            .roomUserCount(roomUserCount)
                            .nickname(ownerUser.getNickname())
                            .build();
                }).toList();

        return resultList;

//        List<GetRoomListResponseDto> resultList;
//
//        if(params.getStatus() == null) {
//             resultList = roomRepository.findAllByIds(roomIdList).stream()
//                    .map(room -> GetRoomListResponseDto.builder()
//                            .roomId(room.getId())
//                            .name(room.getName())
//                            .code(room.getCode())
//                            .startDate(room.getStartDate())
//                            .endDate(room.getEndDate())
//                            .missionPeriod(room.getMissionPeriod())
//                            .status(room.getRoomStatus())
//                            .build())
//                    .toList();
//        }
//        else {
//             resultList = roomRepository.findAllByIdsAndRoomStatus(roomIdList, params.getStatus()).stream()
//                    .map(room -> GetRoomListResponseDto.builder()
//                                    .roomId(room.getId())
//                                    .name(room.getName())
//                                    .code(room.getCode())
//                                    .startDate(room.getStartDate())
//                                    .endDate(room.getEndDate())
//                                    .missionPeriod(room.getMissionPeriod())
//                                    .status(room.getRoomStatus())
//                                    .build())
//                    .toList();
//        }
//
//        return resultList;
    }

    @Transactional(readOnly = true)
    @Override
    public GetRoomDetailsResponseDto getRoomDetails(GetRoomDetailsRequestDto params) {

        Room findRoom = roomUserRepository.findByUserIdAndRoomId(params.getUserId(), params.getRoomId())
                .map(RoomUser::getRoom)
                .orElseThrow(() -> new RoomException(RoomErrorCode.USER_ROOM_INVALID));

        return GetRoomDetailsResponseDto.from(findRoom);
    }

    @Transactional(readOnly = true)
    @Override
    public List<GetRoomUserListResponseDto> getRoomUserList(GetRoomUserListRequestDto params) {

        // 방에 소속된 유저인지 확인
        roomAuthorizationService.checkIsRoomUser(params.getUserId(), params.getRoomId());

        List<GetRoomUserListResponseDto> resultList = roomUserRepository.findAllByRoomIdWithRoomCharacterAndRoomProfileAndUser(params.getRoomId()).stream()
                .map(roomUser -> GetRoomUserListResponseDto.builder()
                        .roomUserId(roomUser.getId())
                        .managerYn(roomUser.getManagerYn())
                        .standbyYn(roomUser.getStandbyYn())
                        .nickname(roomUser.getNickname())
                        .useProfileYn(roomUser.getUseProfileYn())
                        .selfIntroduction(roomUser.getSelfIntroduction())
                        .profileUrl(roomUser.getRoomProfile() != null ? s3Service.generatePresignedUrl(roomUser.getRoomProfile().getImageKey(), accessMinute) : null)
                        .roomCharacterUrl(roomUser.getRoomCharacter() != null ? roomUser.getRoomCharacter().getUrl() : null)
                        .searchId(roomUser.getUser().getSearchId())
                        .build())
                .toList();

        return resultList;
    }

    @Transactional(readOnly = true)
    @Override
    public GetRoomUserDetailsResponseDto getRoomUserDetails(GetRoomUserDetailsRequestDto params) {

        // 방에 소속된 유저인지 확인
        roomAuthorizationService.checkIsRoomUser(params.getUserId(), params.getRoomId());

        RoomUser findRoomUser = roomUserRepository.findByIdAndRoomIdWithRoomCharacterAndRoomProfileAndUser(params.getRoomUserId(), params.getRoomId())
                .orElseThrow(() -> new RoomException(RoomErrorCode.ROOMUSER_ROOM_INVALID));

        return GetRoomUserDetailsResponseDto.builder()
                .roomUserId(findRoomUser.getId())
                .managerYn(findRoomUser.getManagerYn())
                .standbyYn(findRoomUser.getStandbyYn())
                .nickname(findRoomUser.getNickname())
                .useProfileYn(findRoomUser.getUseProfileYn())
                .selfIntroduction(findRoomUser.getSelfIntroduction())
                .profileUrl(findRoomUser.getRoomProfile() != null ? s3Service.generatePresignedUrl(findRoomUser.getRoomProfile().getImageKey(), accessMinute) : null)
                .roomCharacterUrl(findRoomUser.getRoomCharacter() != null ? findRoomUser.getRoomCharacter().getUrl() : null)
                .searchId(findRoomUser.getUser().getSearchId())
                .build();
    }

    @Override
    public CreateRoomResponseDto createRoom(CreateRoomRequestDto params) {

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
                .roomStatus(RoomStatus.WAITING)
                .build();

        // TODO : UserException 생성 시 변경
        User findUser = userRepository.findActiveById(params.getManagerId())
                .orElseThrow(() -> new RuntimeException("해당 유저가 존재하지 않습니다."));

        RoomUser newRoomUser = newRoom.addManagerUser(findUser, params);

        if(params.getUseProfileYn()) {
            String key;
            try {
                key = s3Service.uploadProfileImage(params.getProfileImage(), String.valueOf(params.getManagerId()), S3DirectoryName.ROOMPROFILE.getValue());
            }
            catch (Exception e) {
                throw new RuntimeException("이미지 업로드 실패, " + e.getMessage());
            }

            RoomProfile newRoomProfile = RoomProfile.builder()
                    .imageKey(key)
                    .build();
            newRoomUser.setRoomProfile(newRoomProfile);

        } else {
            RoomCharacter newRoomCharacter = RoomCharacter.builder()
                    .url(systemCharacterColorRepository.findByClothesColorAndSkinColor(params.getClothesColor(), params.getSkinColor())
                            .orElseThrow(() -> new GameException(GameErrorCode.INVALID_COLOR))
                            .getUrl())
                    .build();
            newRoomUser.setRoomProfile(newRoomCharacter);
        }

        roomRepository.save(newRoom);

        return CreateRoomResponseDto.from(newRoom);
    }

    @Override
    public UpdateRoomDetailsResponseDto updateRoomDetails(UpdateRoomDetailsRequestDto params) {

        // 방장인지 권한 확인
        RoomUser findRoomUser = roomAuthorizationService.checkIsManager(params.getUserId(), params.getRoomId());

        // 방 정보 수정
        Room findRoom = roomRepository.findById(findRoomUser.getRoom().getId())
                .orElseThrow(() -> new RoomException(RoomErrorCode.NOT_EXIST_ROOM));

        findRoom.updateRoomInfo(params.getEndDate(), params.getMissionPeriod());

        return UpdateRoomDetailsResponseDto.from(findRoom);
    }

    @Override
    public UpdateRoomStatusStartResponseDto updateRoomStatusStart(UpdateRoomStatusStartRequestDto params) {

        // 방장인지 권한 확인
        roomAuthorizationService.checkIsManager(params.getUserId(), params.getRoomId());

        // 수락 인원이 3명 이하인 경우 시작 불가
        List<RoomUser> acceptedRoomUserList = roomUserRepository.findAllByRoomIdAndStandbyYn(params.getRoomId(), false);
        if(acceptedRoomUserList.size() < 3) {
            throw new RoomException(RoomErrorCode.NOT_ENOUGH_USER_TO_START_ROOM);
        }

        // 방 시작
        Room findRoom = roomRepository.findById(params.getRoomId())
                .orElseThrow(() -> new RoomException(RoomErrorCode.NOT_EXIST_ROOM));

        findRoom.startRoom();

        // 대기상태인 방 유저들 삭제
        List<RoomUser> findRoomUserNotAcceptedList = roomUserRepository.findAllByRoomIdAndStandbyYn(params.getRoomId(), true);
        roomUserRepository.deleteAll(findRoomUserNotAcceptedList);

        // 미션 리스트 생성
        List<RoomMission> newRoomMissionList = params.getMissionList().stream()
                .map(content -> RoomMission.builder()
                        .room(findRoom)
                        .content(content)
                        .executeYn(false)
                        .build())
                .toList();

        roomMissionRepository.saveAll(newRoomMissionList);

        // 마니또, 마니띠 매칭 관계 설정
        Collections.shuffle(acceptedRoomUserList);
        List<Matching> matchingList = new ArrayList<>();
        for(int i=0, size=acceptedRoomUserList.size(); i<size; i++) {

            Matching newMatching;
            if(i == 0) {


                newMatching = Matching.builder()
                        .roomUser(acceptedRoomUserList.get(i))
                        .matchingManitoId(acceptedRoomUserList.get(size-1).getId())
                        .matchingManitiId(acceptedRoomUserList.get(i+1).getId())
                        .build();
            }
            else if(i == size-1) {
                newMatching = Matching.builder()
                        .roomUser(acceptedRoomUserList.get(i))
                        .matchingManitoId(acceptedRoomUserList.get(i-1).getId())
                        .matchingManitiId(acceptedRoomUserList.get(0).getId())
                        .build();
            }
            else {
                newMatching = Matching.builder()
                        .roomUser(acceptedRoomUserList.get(i))
                        .matchingManitoId(acceptedRoomUserList.get(i-1).getId())
                        .matchingManitiId(acceptedRoomUserList.get(i+1).getId())
                        .build();
            }

            matchingList.add(newMatching);
        }

        matchingRepository.saveAll(matchingList);

        // 방 유저들 간 채팅 생성
        List<ChattingRoom> chattingRoomList = new ArrayList<>();
        ChattingRoom allChattingRoom = ChattingRoom.builder().build();
        chattingRoomList.add(allChattingRoom);

        List<ChattingParticipate> chattingParticipateList = new ArrayList<>();
        for(RoomUser roomUser : acceptedRoomUserList) {
            ChattingRoom newChattingRoom = ChattingRoom.builder()
                    .build();

            Matching matchingInfo = matchingRepository.findByRoomUserId(roomUser.getId());

            ChattingParticipate manitoChattingParticipate = ChattingParticipate.builder()
                    .chattingUserType(ChattingParticipateType.MANITO)
                    .chattingRoom(newChattingRoom)
                    .roomUser(roomUserRepository.findById(matchingInfo.getMatchingManitoId())
                            .orElseThrow(() -> new RoomException(RoomErrorCode.NOT_EXIST_ROOM_USER)))
                    .build();

            ChattingParticipate manitiChattingParticipate = ChattingParticipate.builder()
                    .chattingUserType(ChattingParticipateType.MANITI)
                    .chattingRoom(newChattingRoom)
                    .roomUser(roomUserRepository.findById(matchingInfo.getMatchingManitiId())
                            .orElseThrow(() -> new RoomException(RoomErrorCode.NOT_EXIST_ROOM_USER)))
                    .build();

            ChattingParticipate allChattingParticipate = ChattingParticipate.builder()
                    .chattingUserType(ChattingParticipateType.ALL)
                    .chattingRoom(allChattingRoom)
                    .roomUser(roomUser)
                    .build();

            chattingRoomList.add(newChattingRoom);
            chattingParticipateList.add(manitoChattingParticipate);
            chattingParticipateList.add(manitiChattingParticipate);
            chattingParticipateList.add(allChattingParticipate);
        }

        chattingRoomRepository.saveAll(chattingRoomList);
        chattingParticipateRepository.saveAll(chattingParticipateList);

        return UpdateRoomStatusStartResponseDto.from(findRoom);
    }

    @Override
    public void updateRoomStatusEnd(UpdateRoomStatusEndRequestDto params) {

        // 방장인지 권한 확인
        roomAuthorizationService.checkIsManager(params.getUserId(), params.getRoomId());

        Room findRoom = roomRepository.findById(params.getRoomId())
                .orElseThrow(() -> new RoomException(RoomErrorCode.NOT_EXIST_ROOM));

        findRoom.terminateRoom();

        // TODO: 방 히스토리 저장, 필요없는 잔여 데이터 삭제

    }

    @Transactional(readOnly = true)
    @Override
    public EnterRoomByCodeResponseDto enterRoomByCode(EnterRoomByCodeRequestDto params) {

        Room findRoom = roomRepository.findByCode(params.getCode())
                .orElseThrow(() -> new RoomException(RoomErrorCode.NOT_EXIST_ROOM_CODE));

        if(roomUserRepository.findByUserIdAndRoomId(params.getUserId(), findRoom.getId()).isPresent()) {
            throw new RoomException(RoomErrorCode.ALREADY_IN_ROOM);
        }

        return EnterRoomByCodeResponseDto.from(findRoom);
    }

    @Override
    public CreateRoomUserProfileResponseDto createRoomUserProfile(CreateRoomUserProfileRequestDto params) {

        if(roomUserRepository.findByUserIdAndRoomId(params.getUserId(), params.getRoomId()).isPresent()) {
            throw new RoomException(RoomErrorCode.ALREADY_IN_ROOM);
        }

        User findUser = userRepository.findActiveById(params.getUserId())
                .orElseThrow(() -> new RuntimeException("해당 유저가 존재하지 않습니다.")); // TODO

        Room findRoom = roomRepository.findById(params.getRoomId())
                .orElseThrow(() -> new RoomException(RoomErrorCode.NOT_EXIST_ROOM));

        RoomUser newRoomUser = findRoom.addRoomUser(findUser, params);

        if(params.getUseProfileYn()) {

            String key;
            try {
                key = s3Service.uploadProfileImage(params.getProfileImage(), String.valueOf(newRoomUser.getId()), S3DirectoryName.ROOMPROFILE.getValue());
            }
            catch (Exception e) {
                throw new RuntimeException("이미지 업로드 실패, " + e.getMessage());
            }
            // TODO: S3 Storage에 파일 업로드 이후 url 반환받아 roomProfile 생성 이후 저장
            RoomProfile newRoomProfile = RoomProfile.builder()
                    .imageKey(key)
                    .build();
            newRoomUser.setRoomProfile(newRoomProfile);

        } else {
            RoomCharacter newRoomCharacter = RoomCharacter.builder()
                    .url(systemCharacterColorRepository.findByClothesColorAndSkinColor(params.getClothesColor(), params.getSkinColor())
                            .orElseThrow(() -> new GameException(GameErrorCode.INVALID_COLOR))
                            .getUrl())
                    .build();
            newRoomUser.setRoomProfile(newRoomCharacter);
        }

        roomUserRepository.save(newRoomUser);

        return CreateRoomUserProfileResponseDto.from(newRoomUser);
    }

    @Override
    public UpdateRoomUserSelfIntroductionResponseDto updateRoomUserSelfIntroduction(UpdateRoomUserSelfIntroductionRequestDto params) {

        // 방에 소속된 유저인지 확인
        RoomUser findRoomUser = roomAuthorizationService.checkIsRoomUser(params.getUserId(), params.getRoomId());

        findRoomUser.changeSelfIntroduction(params.getSelfIntroduction());

        return UpdateRoomUserSelfIntroductionResponseDto.from(findRoomUser);
    }

    @Override
    public UpdateRoomUserStatusAcceptedResponseDto updateRoomUserStatusAccepted(UpdateRoomUserStatusAcceptedRequestDto params) {

        // 방장인지 권한 확인
        roomAuthorizationService.checkIsManager(params.getUserId(), params.getRoomId());

        RoomUser findRoomUser = roomUserRepository.findById(params.getRoomUserId())
                .orElseThrow(() -> new RoomException(RoomErrorCode.NOT_EXIST_ROOM_USER));

        findRoomUser.acceptedIntoRoom();

        return UpdateRoomUserStatusAcceptedResponseDto.from(findRoomUser);
    }

    @Override
    public void deleteRoomUserDenied(DeleteRoomUserDeniedRequestDto params) {

        // 방장인지 권한 확인
        roomAuthorizationService.checkIsManager(params.getUserId(), params.getRoomId());

        RoomUser findRoomUser = roomUserRepository.findById(params.getRoomUserId())
                .orElseThrow(() -> new RoomException(RoomErrorCode.NOT_EXIST_ROOM_USER));

        if(!findRoomUser.getStandbyYn()) {
            throw new RoomException(RoomErrorCode.CANNOT_DENY_ROOM_USER);
        }

        roomUserRepository.delete(findRoomUser);
    }


}
