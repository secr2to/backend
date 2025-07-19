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
import com.emelmujiro.secreto.notification.dto.request.SaveNotificationRequestDto;
import com.emelmujiro.secreto.notification.dto.request.SendNotificationRequestDto;
import com.emelmujiro.secreto.notification.entity.NotificationType;
import com.emelmujiro.secreto.notification.service.NotificationService;
import com.emelmujiro.secreto.room.dto.DeleteRoomRequestDto;
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
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Transactional
@Service
public class RoomServiceImpl implements RoomService {

    @Value("${s3.access-minute}")
    private int accessMinute;

    @Value("${server.url}")
    private String serverUrl;

    @Value("${image.route}")
    private String imageRoute;

    private final RoomUserRepository roomUserRepository;
    private final RoomRepository roomRepository;
    private final UserRepository userRepository;
    private final RoomMissionRepository roomMissionRepository;
    private final SystemCharacterColorRepository systemCharacterColorRepository;
    private final MatchingRepository matchingRepository;
    private final ChattingRoomRepository chattingRoomRepository;
    private final ChattingParticipateRepository chattingParticipateRepository;

    private final RoomAuthorizationService roomAuthorizationService;
    private final NotificationService notificationService;

    private final S3Service s3Service;

    private final SimpMessagingTemplate messagingTemplate;

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
                    String imageUrl = room.getImageKey() == null ? null : s3Service.generatePresignedUrl(room.getImageKey(), accessMinute);

                    return GetRoomListResponseDto.builder()
                            .roomId(room.getId())
                            .name(room.getName())
                            .code(room.getCode())
                            .startDate(room.getStartDate())
                            .endDate(room.getEndDate())
                            .missionPeriod(room.getMissionPeriod())
                            .status(room.getRoomStatus())
                            .imageUrl(imageUrl)
                            .roomUserCount(roomUserCount)
                            .nickname(ownerUser.getNickname())
                            .build();
                }).toList();

        return resultList;
    }

    @Transactional(readOnly = true)
    @Override
    public GetRoomDetailsResponseDto getRoomDetails(GetRoomDetailsRequestDto params) {

        Room findRoom = roomUserRepository.findByUserIdAndRoomId(params.getUserId(), params.getRoomId())
                .map(RoomUser::getRoom)
                .orElseThrow(() -> new RoomException(RoomErrorCode.USER_ROOM_INVALID));

        String imageUrl = findRoom.getImageKey() == null ? null : s3Service.generatePresignedUrl(findRoom.getImageKey(), accessMinute);

        return GetRoomDetailsResponseDto.from(findRoom, imageUrl);
    }

    @Transactional(readOnly = true)
    @Override
    public List<GetRoomUserListResponseDto> getRoomUserList(GetRoomUserListRequestDto params) {

        // 방에 소속된 유저인지 확인
        roomAuthorizationService.checkIsRoomUser(params.getUserId(), params.getRoomId());

        return roomUserRepository.findAllByRoomIdWithRoomCharacterAndRoomProfileAndUser(params.getRoomId()).stream()
                .map(roomUser -> GetRoomUserListResponseDto.builder()
                        .roomUserId(roomUser.getId())
                        .managerYn(roomUser.getManagerYn())
                        .standbyYn(roomUser.getStandbyYn())
                        .nickname(roomUser.getNickname())
                        .selfIntroduction(roomUser.getSelfIntroduction())
                        .searchId(roomUser.getUser().getSearchId())
                        .build())
                .toList();
    }

    @Override
    public List<GetRoomUserProfileListResponseDto> getRoomUserProfileList(GetRoomUserProfileListRequestDto params) {

        // 방에 소속된 유저인지 확인
        roomAuthorizationService.checkIsRoomUser(params.getUserId(), params.getRoomId());

        return roomUserRepository.findAllByRoomIdWithRoomCharacterAndRoomProfileAndUser(params.getRoomId()).stream()
                .map(roomUser -> {

                    String profileUrl;
                    if (roomUser.getUseProfileYn()) {
                        profileUrl = s3Service.generatePresignedUrl(roomUser.getRoomProfile().getImageKey(), accessMinute);
                    } else {
                        profileUrl = serverUrl + imageRoute + roomUser.getRoomCharacter().getUrl();
                    }

                    return GetRoomUserProfileListResponseDto.builder()
                            .roomUserId(roomUser.getId())
                            .profileUrl(profileUrl)
                            .build();
                })
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public GetRoomUserDetailsResponseDto getRoomUserDetails(GetRoomUserDetailsRequestDto params) {

        // 방에 소속된 유저인지 확인
        roomAuthorizationService.checkIsRoomUser(params.getUserId(), params.getRoomId());

        RoomUser findRoomUser = roomUserRepository.findByIdAndRoomIdWithRoomCharacterAndRoomProfileAndUser(params.getRoomUserId(), params.getRoomId())
                .orElseThrow(() -> new RoomException(RoomErrorCode.ROOMUSER_ROOM_INVALID));

        return GetRoomUserDetailsResponseDto.from(findRoomUser);
    }

    @Override
    public GetRoomUserProfileDetailsResponseDto getRoomUserProfileDetails(GetRoomUserProfileDetailsRequestDto params) {

        // 방에 소속된 유저인지 확인
        roomAuthorizationService.checkIsRoomUser(params.getUserId(), params.getRoomId());

        RoomUser findRoomUser = roomUserRepository.findByIdAndRoomIdWithRoomCharacterAndRoomProfileAndUser(params.getRoomUserId(), params.getRoomId())
                .orElseThrow(() -> new RoomException(RoomErrorCode.ROOMUSER_ROOM_INVALID));

        String profileUrl;
        if (findRoomUser.getUseProfileYn()) {
            profileUrl = s3Service.generatePresignedUrl(findRoomUser.getRoomProfile().getImageKey(), accessMinute);
        } else {
            profileUrl = serverUrl + imageRoute + findRoomUser.getRoomCharacter().getUrl();
        }

        return GetRoomUserProfileDetailsResponseDto.builder()
                .roomUserId(findRoomUser.getId())
                .profileUrl(profileUrl)
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
                key = s3Service.uploadProfileImage(params.getProfileImage(), String.valueOf(params.getManagerId()), S3DirectoryName.ROOM_USER_PROFILE.getValue());
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

        notificationService.sendNotification(SendNotificationRequestDto.builder()
                .notificationType(NotificationType.ROOM_INFORMATION)
                .content(findRoom.getName() + " " + NotificationType.ROOM_INFORMATION.getMessage())
                .targetId(findRoom.getId())
                .author(findRoom.getName())
                .build());

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

        List<User> receiverList = new ArrayList<>();
        for(RoomUser roomUser : acceptedRoomUserList) {
            receiverList.add(roomUser.getUser());
        }

        // 방 시작 알림 전송 및 저장
        notificationService.sendNotification(SendNotificationRequestDto.builder()
                .notificationType(NotificationType.ROOM_START)
                .content(findRoom.getName() + " " + NotificationType.ROOM_START.getMessage())
                .author(findRoom.getName())
                .targetId(findRoom.getId())
                .build());

        notificationService.saveNotification(SaveNotificationRequestDto.builder()
                .notificationType(NotificationType.ROOM_START)
                .author(findRoom.getName())
                .content(findRoom.getName() + " " + NotificationType.ROOM_START.getMessage())
                .targetId(findRoom.getId())
                .receiverList(receiverList)
                .room(findRoom)
                .referenceId(findRoom.getId())
                .build());

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

        // 방 시작 시 미션 1개 부여 TODO : 알림 적용해야 함
        List<RoomMission> missionList = roomMissionRepository.findAllByRoomIdAndExecuteYn(findRoom.getId(), false);
        RoomMission selectedMission = missionList.get(new Random().nextInt(missionList.size()));
        selectedMission.executeMission();

        // 방 미션 제시 알림 전송 및 저장
        notificationService.sendNotification(SendNotificationRequestDto.builder()
                .notificationType(NotificationType.MISSION)
                .content(findRoom.getName() + " " + NotificationType.MISSION.getMessage())
                .author(findRoom.getName())
                .targetId(findRoom.getId())
                .build());

        notificationService.saveNotification(SaveNotificationRequestDto.builder()
                .notificationType(NotificationType.MISSION)
                .author(findRoom.getName())
                .content(selectedMission.getContent() + " " + NotificationType.MISSION.getMessage())
                .targetId(findRoom.getId())
                .receiverList(receiverList)
                .room(findRoom)
                .referenceId(findRoom.getId())
                .build());

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

        List<RoomUser> roomUserList = roomUserRepository.findAllByRoomIdAndStandbyYn(findRoom.getId(), false);

        List<User> userList = new ArrayList<>();
        for(RoomUser roomUser : roomUserList) {
            userList.add(roomUser.getUser());
        }
        notificationService.sendNotification(SendNotificationRequestDto.builder()
                .notificationType(NotificationType.ROOM_END)
                .content(findRoom.getName() + " " + NotificationType.ROOM_END.getMessage())
                .author(findRoom.getName())
                .targetId(findRoom.getId())
                .build());

        notificationService.saveNotification(SaveNotificationRequestDto.builder()
                .notificationType(NotificationType.ROOM_END)
                .author(findRoom.getName())
                .content(findRoom.getName() + " " + NotificationType.ROOM_END.getMessage())
                .targetId(findRoom.getId())
                .receiverList(userList)
                .room(findRoom)
                .referenceId(findRoom.getId())
                .build());

        // TODO: 방 히스토리 저장

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
                key = s3Service.uploadProfileImage(params.getProfileImage(), String.valueOf(newRoomUser.getId()), S3DirectoryName.ROOM_USER_PROFILE.getValue());
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

        roomUserRepository.save(newRoomUser);

        notificationService.sendNotification(SendNotificationRequestDto.builder()
                .notificationType(NotificationType.INGAME_PROFILE_INFO)
                .content(NotificationType.INGAME_PROFILE_INFO.getMessage())
                .targetId(findRoom.getId())
                .author(findRoom.getName())
                .build());

        return CreateRoomUserProfileResponseDto.from(newRoomUser);
    }

    @Override
    public UpdateRoomUserSelfIntroductionResponseDto updateRoomUserSelfIntroduction(UpdateRoomUserSelfIntroductionRequestDto params) {

        Room findRoom = roomRepository.findById(params.getRoomId())
                .orElseThrow(() -> new RoomException(RoomErrorCode.NOT_EXIST_ROOM));

        // 방에 소속된 유저인지 확인
        RoomUser findRoomUser = roomAuthorizationService.checkIsRoomUser(params.getUserId(), params.getRoomId());

        findRoomUser.changeSelfIntroduction(params.getSelfIntroduction());

        notificationService.sendNotification(SendNotificationRequestDto.builder()
                .notificationType(NotificationType.INGAME_INTRODUCTION)
                .content(NotificationType.INGAME_INTRODUCTION.getMessage())
                .targetId(findRoom.getId())
                .author(findRoom.getName())
                .build());

        return UpdateRoomUserSelfIntroductionResponseDto.from(findRoomUser);
    }

    @Override
    public List<UpdateRoomUserStatusAcceptedResponseDto> updateRoomUserStatusAccepted(UpdateRoomUserStatusAcceptedRequestDto params) {

        Room findRoom = roomRepository.findById(params.getRoomId())
                .orElseThrow(() -> new RoomException(RoomErrorCode.NOT_EXIST_ROOM));

        // 방장인지 권한 확인
        roomAuthorizationService.checkIsManager(params.getUserId(), params.getRoomId());

        // 요청한 유저가 모두 방에 속해 있는지 검증
        Set<Long> roomUserIdSet = findRoom.getRoomUserList().stream()
                .map(RoomUser::getId)
                .collect(Collectors.toSet());

        for (Long roomUserId : params.getRoomUserIds()) {
            if (!roomUserIdSet.contains(roomUserId)) {
                throw new RoomException(RoomErrorCode.NOT_EXIST_ROOM_USER);
            }
        }

        List<RoomUser> findRoomUserList = roomUserRepository.findAllById(params.getRoomUserIds());

        for(RoomUser roomUser : findRoomUserList) {
            roomUser.acceptedIntoRoom();
        }

        notificationService.sendNotification(SendNotificationRequestDto.builder()
                .notificationType(NotificationType.USER_ACCEPT)
                .content(NotificationType.USER_ACCEPT.getMessage())
                .targetId(findRoom.getId())
                .author(findRoom.getName())
                .build());

        return UpdateRoomUserStatusAcceptedResponseDto.from(findRoomUserList);
    }

    @Override
    public void deleteRoomUserDenied(DeleteRoomUserDeniedRequestDto params) {

        Room findRoom = roomRepository.findById(params.getRoomId())
                .orElseThrow(() -> new RoomException(RoomErrorCode.NOT_EXIST_ROOM));

        // 방장인지 권한 확인
        roomAuthorizationService.checkIsManager(params.getUserId(), params.getRoomId());

        List<RoomUser> findRoomUserList = roomUserRepository.findAllById(params.getRoomUserIds());

        for(RoomUser roomUser : findRoomUserList) {
            if(!roomUser.getStandbyYn()) {
                throw new RoomException(RoomErrorCode.CANNOT_DENY_ROOM_USER);
            }
        }

        notificationService.sendNotification(SendNotificationRequestDto.builder()
                .notificationType(NotificationType.USER_REJECT)
                .content(NotificationType.USER_REJECT.getMessage())
                .targetId(findRoom.getId())
                .author(findRoom.getName())
                .build());

        roomUserRepository.deleteAll(findRoomUserList);
    }

    @Override
    public GetMyRoomUserRoleResponseDto getMyRoomUserRole(GetMyRoomUserRoleRequestDto params) {

        RoomUser findRoomUser = roomAuthorizationService.checkIsRoomUser(params.getUserId(), params.getRoomId());

        return GetMyRoomUserRoleResponseDto.from(findRoomUser);
    }

    @Override
    public UpdateRoomImageResponseDto updateRoomImage(UpdateRoomImageRequestDto params) {

        // 방장인지 권한 확인
        RoomUser manager = roomAuthorizationService.checkIsManager(params.getUserId(), params.getRoomId());

        String key;
        try {
            key = s3Service.uploadProfileImage(params.getRoomImage(), String.valueOf(manager.getId()), S3DirectoryName.ROOM_IMAGE.getValue());
        }
        catch (Exception e) {
            throw new RuntimeException("이미지 업로드 실패, " + e.getMessage());
        }

        Room findRoom = roomRepository.findById(params.getRoomId())
                .orElseThrow(() -> new RoomException(RoomErrorCode.NOT_EXIST_ROOM));

        if(findRoom.getImageKey() != null) {
            try {
                s3Service.deleteFile(findRoom.getImageKey());
            } catch (Exception e) {
                throw new RuntimeException("기존 이미지 삭제 중 오류 발생");
            }
        }

        findRoom.changeRoomImageKey(key);

        notificationService.sendNotification(SendNotificationRequestDto.builder()
                .notificationType(NotificationType.ROOM_IMAGE)
                .content(NotificationType.ROOM_IMAGE.getMessage())
                .targetId(findRoom.getId())
                .author(findRoom.getName())
                .build());

        return UpdateRoomImageResponseDto.from(findRoom);
    }

    @Override
    public void deleteRoom(DeleteRoomRequestDto params) {

        // 방장인지 권한 확인
        roomAuthorizationService.checkIsManager(params.getUserId(), params.getRoomId());

        Room findRoom = roomRepository.findById(params.getRoomId())
                .orElseThrow(() -> new RoomException(RoomErrorCode.NOT_EXIST_ROOM));

        if(findRoom.getRoomStatus() == RoomStatus.PROGRESS) {
            throw new RoomException(RoomErrorCode.ROOM_STILL_IN_PROGRESS);
        }

        roomRepository.delete(findRoom);
    }

    @Override
    public GetMyRoomUserDetailsResponseDto getMyRoomUserDetails(GetMyRoomUserDetailsRequestDto params) {

        // 방에 소속된 유저인지 확인
        RoomUser findRoomUser = roomAuthorizationService.checkIsRoomUser(params.getUserId(), params.getRoomId());

        String profileUrl;
        if (findRoomUser.getUseProfileYn()) {
            profileUrl = s3Service.generatePresignedUrl(findRoomUser.getRoomProfile().getImageKey(), accessMinute);
        } else {
            profileUrl = serverUrl + imageRoute + findRoomUser.getRoomCharacter().getUrl();
        }

        return GetMyRoomUserDetailsResponseDto.builder()
                .roomUserId(findRoomUser.getId())
                .managerYn(findRoomUser.getManagerYn())
                .standbyYn(findRoomUser.getStandbyYn())
                .nickname(findRoomUser.getNickname())
                .selfIntroduction(findRoomUser.getSelfIntroduction())
                .profileUrl(profileUrl)
                .searchId(findRoomUser.getUser().getSearchId())
                .build();
    }


}