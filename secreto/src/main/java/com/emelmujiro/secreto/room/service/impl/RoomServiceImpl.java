package com.emelmujiro.secreto.room.service.impl;

import com.emelmujiro.secreto.room.dto.request.*;
import com.emelmujiro.secreto.room.dto.response.*;
import com.emelmujiro.secreto.room.entity.Room;
import com.emelmujiro.secreto.room.entity.RoomUser;
import com.emelmujiro.secreto.room.error.RoomErrorCode;
import com.emelmujiro.secreto.room.exception.RoomException;
import com.emelmujiro.secreto.room.repository.RoomRepository;
import com.emelmujiro.secreto.room.repository.RoomUserRepository;
import com.emelmujiro.secreto.room.service.RoomService;
import com.emelmujiro.secreto.user.entity.User;
import com.emelmujiro.secreto.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    @Override
    public GetRoomUserDetailsResDto getRoomUserDetails(GetRoomUserDetailsReqDto params) {

        RoomUser checkRoomUser = roomUserRepository.findByUserIdAndRoomId(params.getUserId(), params.getRoomId())
                .orElseThrow(() -> new RoomException(RoomErrorCode.USER_ROOM_INVALID));

        RoomUser findRoomUser = roomUserRepository.findByIdAndRoomIdWithRoomCharacterAndRoomProfile(params.getRoomUserId(), params.getRoomId())
                .orElseThrow(() -> new RoomException(RoomErrorCode.ROOMUSER_ROOM_INVALID));

        return GetRoomUserDetailsResDto.from(findRoomUser);
    }

    @Override
    public CreateRoomResDto createRoom(CreateRoomReqDto params) {

        String generatedcode = "";
        boolean isCodeExists = true;
        while(isCodeExists) {

            generatedcode = generateRandomCode();

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

    /**
     * 방 입장 코드 생성 메서드
     */
    public String generateRandomCode() {
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 6;
        Random random = new Random();
        String generatedCode = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();

        return generatedCode;
    }
}
