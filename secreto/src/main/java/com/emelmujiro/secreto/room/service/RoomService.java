package com.emelmujiro.secreto.room.service;

import com.emelmujiro.secreto.room.dto.request.*;
import com.emelmujiro.secreto.room.dto.response.*;

import java.util.List;

public interface RoomService {
    List<GetRoomListResDto> getRoomList(GetRoomListReqDto params);

    GetRoomDetailsResDto getRoomDetails(GetRoomDetailsReqDto params);

    List<GetRoomUserListResDto> getRoomUserList(GetRoomUserListReqDto params);

    GetRoomUserDetailsResDto getRoomUserDetails(GetRoomUserDetailsReqDto params);

    CreateRoomResDto createRoom(CreateRoomReqDto params);

    UpdateRoomDetailsResDto updateRoomDetails(UpdateRoomDetailsReqDto params);
}
