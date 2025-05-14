package com.emelmujiro.secreto.room.service.impl;

import com.emelmujiro.secreto.room.dto.request.GetRoomListReqDto;
import com.emelmujiro.secreto.room.dto.response.GetRoomListResDto;
import com.emelmujiro.secreto.room.entity.RoomUser;
import com.emelmujiro.secreto.room.repository.RoomRepository;
import com.emelmujiro.secreto.room.repository.RoomUserRepository;
import com.emelmujiro.secreto.room.service.RoomService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional
@Service
public class RoomServiceImpl implements RoomService {

    private final RoomUserRepository roomUserRepository;
    private final RoomRepository roomRepository;

    @Override
    public List<GetRoomListResDto> getRoomList(GetRoomListReqDto params) {

        List<RoomUser> findRoomUserList = roomUserRepository.findAllByUserId(params.getUserId());

        List<Long> roomIdList = new ArrayList<>();
        findRoomUserList.stream()
                .forEach(roomUser -> roomIdList.add(roomUser.getRoom().getId()));

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
}
