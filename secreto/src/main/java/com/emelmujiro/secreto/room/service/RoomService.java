package com.emelmujiro.secreto.room.service;

import com.emelmujiro.secreto.room.dto.request.GetRoomDetailsReqDto;
import com.emelmujiro.secreto.room.dto.request.GetRoomListReqDto;
import com.emelmujiro.secreto.room.dto.response.GetRoomDetailsResDto;
import com.emelmujiro.secreto.room.dto.response.GetRoomListResDto;

import java.util.List;

public interface RoomService {
    List<GetRoomListResDto> getRoomList(GetRoomListReqDto params);

    GetRoomDetailsResDto getRoomDetails(GetRoomDetailsReqDto params);
}
