package com.emelmujiro.secreto.room.service;

import com.emelmujiro.secreto.room.dto.request.GetRoomDetailsReqDto;
import com.emelmujiro.secreto.room.dto.request.GetRoomListReqDto;
import com.emelmujiro.secreto.room.dto.request.GetRoomUserDetailsReqDto;
import com.emelmujiro.secreto.room.dto.request.GetRoomUserListReqDto;
import com.emelmujiro.secreto.room.dto.response.GetRoomDetailsResDto;
import com.emelmujiro.secreto.room.dto.response.GetRoomListResDto;
import com.emelmujiro.secreto.room.dto.response.GetRoomUserDetailsResDto;
import com.emelmujiro.secreto.room.dto.response.GetRoomUserListResDto;

import java.util.List;

public interface RoomService {
    List<GetRoomListResDto> getRoomList(GetRoomListReqDto params);

    GetRoomDetailsResDto getRoomDetails(GetRoomDetailsReqDto params);

    List<GetRoomUserListResDto> getRoomUserList(GetRoomUserListReqDto params);

    GetRoomUserDetailsResDto getRoomUserDetails(GetRoomUserDetailsReqDto params);
}
