package com.emelmujiro.secreto.room.controller;

import com.emelmujiro.secreto.room.dto.request.GetRoomListReqDto;
import com.emelmujiro.secreto.room.dto.response.GetRoomListResDto;
import com.emelmujiro.secreto.room.entity.RoomStatus;
import com.emelmujiro.secreto.room.service.RoomService;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class RoomControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoomService roomService;

    @Test
    void getRoomList_success() throws Exception {

        // given
        GetRoomListReqDto param = GetRoomListReqDto.builder()
                .userId(1L)
                .status(RoomStatus.PROGRESS)
                .build();

        GetRoomListResDto room1 = GetRoomListResDto.builder()
                .roomId(1L)
                .name("싸피 13반 방")
                .code("YYSDFD")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now().plusMonths(1))
                .missionPeriod(3)
                .status(RoomStatus.PROGRESS)
                .build();

        GetRoomListResDto room2 = GetRoomListResDto.builder()
                .roomId(2L)
                .name("마니또할 사람")
                .code("YYYSDK")
                .startDate(LocalDateTime.now().minusDays(3))
                .endDate(LocalDateTime.now().plusDays(20))
                .missionPeriod(3)
                .status(RoomStatus.PROGRESS)
                .build();

        List<GetRoomListResDto> mockList = Arrays.asList(room1, room2);

        when(roomService.getRoomList(any(GetRoomListReqDto.class))).thenReturn(mockList);

        // when, then
        mockMvc.perform(get("/rooms?status=PROGRESS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("방 목록을 조회하였습니다."))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data", Matchers.hasSize(2)))
                .andExpect(jsonPath("$.data[0].name").value("싸피 13반 방"))
                .andExpect(jsonPath("$.data[0].code").value("YYSDFD"))
                .andExpect(jsonPath("$.data[1].name").value("마니또할 사람"))
                .andExpect(jsonPath("$.data[1].code").value("YYYSDK"))
                .andExpect(jsonPath("$.data[1].status").value("PROGRESS"));
    }
}