package com.emelmujiro.secreto.room.service.impl;


import com.emelmujiro.secreto.room.entity.RoomUser;
import com.emelmujiro.secreto.room.error.RoomErrorCode;
import com.emelmujiro.secreto.room.exception.RoomException;
import com.emelmujiro.secreto.room.repository.RoomUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Component
@Transactional(readOnly = true)
public class RoomAuthorizationService {

    private final RoomUserRepository roomUserRepository;

    // 방에 소속된 유저인지 확인
    public RoomUser checkIsRoomUser(long userId, long roomId) {

        RoomUser findRoomUser = roomUserRepository.findByUserIdAndRoomId(userId, roomId)
                .orElseThrow(() -> new RoomException(RoomErrorCode.USER_ROOM_INVALID));

        return findRoomUser;
    }

    // 방장인지 권한 확인
    public RoomUser checkIsManager(Long userId, Long roomId) {

        RoomUser findRoomUser = roomUserRepository.findByUserIdAndRoomId(userId, roomId)
                .orElseThrow(() -> new RoomException(RoomErrorCode.USER_ROOM_INVALID));

        if(!findRoomUser.getManagerYn()) {
            throw new RoomException(RoomErrorCode.INVAILD_AUTHORITY);
        }

        return findRoomUser;
    }

}