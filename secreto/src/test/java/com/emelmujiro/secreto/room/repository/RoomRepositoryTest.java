package com.emelmujiro.secreto.room.repository;

import com.emelmujiro.secreto.room.entity.Room;
import com.emelmujiro.secreto.room.entity.RoomStatus;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Transactional
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class RoomRepositoryTest {

    @Autowired
    private RoomRepository roomRepository;

    @Test
    void findAllByIdsAndRoomStatus() {

        // given
        Room room1 = Room.builder()
                .name("싸피13반 모여라")
                .roomStatus(RoomStatus.PROGRESS)
                .code("SDLKFJDSF")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusMonths(1))
                .missionPeriod(3)
                .build();

        Room room2 = Room.builder()
                .name("아무나 모여라")
                .roomStatus(RoomStatus.PROGRESS)
                .code("SDLKFJDSD")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusMonths(1))
                .missionPeriod(3)
                .build();

        Room room3 = Room.builder()
                .name("아무나 모여라2")
                .roomStatus(RoomStatus.WAITING)
                .code("SDLKFJDSH")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusMonths(1))
                .missionPeriod(3)
                .build();

        List<Room> rooms = new ArrayList<>();
        rooms.add(room1);
        rooms.add(room2);
        rooms.add(room3);

        roomRepository.saveAll(rooms);

        // when
        List<Room> resultList = roomRepository.findAllByIdsAndRoomStatus(List.of(room1.getId(), room2.getId(), room3.getId()), RoomStatus.PROGRESS);

        // then
        assertEquals(2, resultList.size());
        assertEquals("싸피13반 모여라", resultList.get(0).getName());
        assertEquals(RoomStatus.PROGRESS, resultList.get(0).getRoomStatus());
        assertEquals("아무나 모여라", resultList.get(1).getName());
        assertEquals(RoomStatus.PROGRESS, resultList.get(1).getRoomStatus());
    }
}