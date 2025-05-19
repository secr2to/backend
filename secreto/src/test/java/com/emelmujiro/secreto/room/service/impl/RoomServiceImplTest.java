package com.emelmujiro.secreto.room.service.impl;

import com.emelmujiro.secreto.room.dto.request.GetRoomListRequestDto;
import com.emelmujiro.secreto.room.dto.response.GetRoomListResponseDto;
import com.emelmujiro.secreto.room.entity.Room;
import com.emelmujiro.secreto.room.entity.RoomStatus;
import com.emelmujiro.secreto.room.entity.RoomUser;
import com.emelmujiro.secreto.room.repository.RoomRepository;
import com.emelmujiro.secreto.room.repository.RoomUserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RoomServiceImplTest {

    @Mock
    private RoomRepository roomRepository;

    @Mock
    private RoomUserRepository roomUserRepository;

    @InjectMocks
    private RoomServiceImpl roomServiceImpl;

    @Test
    void getRoomList() {

        // given
        GetRoomListRequestDto param = GetRoomListRequestDto.builder()
                .userId(1L)
                .status(RoomStatus.PROGRESS)
                .build();

        Room room1 = Room.builder()
                .id(1L)
                .name("싸피13반 모여라")
                .roomStatus(RoomStatus.PROGRESS)
                .code("SDLKFJDSF")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusMonths(1))
                .missionPeriod(3)
                .build();

        Room room2 = Room.builder()
                .id(2L)
                .name("아무나 모여라")
                .roomStatus(RoomStatus.PROGRESS)
                .code("SDLKFJDSD")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusMonths(1))
                .missionPeriod(3)
                .build();

        Room room3 = Room.builder()
                .id(3L)
                .name("아무나 모여라2")
                .roomStatus(RoomStatus.WAITING)
                .code("SDLKFJDSD")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusMonths(1))
                .missionPeriod(3)
                .build();

        RoomUser roomUser1 = RoomUser.builder()
                .id(1L)
                .room(room1)
                .managerYn(true)
                .standbyYn(false)
                .nickname("하악찡")
                .useProfileYn(false)
                .selfIntroduction("하이용")
                .build();

        RoomUser roomUser2 = RoomUser.builder()
                .id(2L)
                .room(room2)
                .managerYn(false)
                .standbyYn(false)
                .nickname("하악찡")
                .useProfileYn(false)
                .selfIntroduction("하이용")
                .build();

        RoomUser roomUser3 = RoomUser.builder()
                .id(3L)
                .room(room3)
                .managerYn(false)
                .standbyYn(false)
                .nickname("하악찡")
                .useProfileYn(false)
                .selfIntroduction("하이용")
                .build();

        when(roomUserRepository.findAllByUserId(param.getUserId())).thenReturn(Arrays.asList(roomUser1, roomUser2, roomUser3));
        when(roomRepository.findAllByIdsAndRoomStatus(Arrays.asList(room1.getId(), room2.getId(), room3.getId()), param.getStatus())).thenReturn(Arrays.asList(room1, room2));

        // when
        List<GetRoomListResponseDto> resultList = roomServiceImpl.getRoomList(param);

        // then
        assertEquals(2, resultList.size());
        assertEquals(1L, resultList.get(0).getRoomId());
        assertEquals("싸피13반 모여라", resultList.get(0).getName());
        assertEquals("SDLKFJDSF", resultList.get(0).getCode());
        assertEquals(3, resultList.get(0).getMissionPeriod());
        assertEquals("아무나 모여라", resultList.get(1).getName());
        assertEquals("SDLKFJDSD", resultList.get(1).getCode());

    }
}