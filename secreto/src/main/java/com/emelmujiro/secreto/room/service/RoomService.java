package com.emelmujiro.secreto.room.service;

import com.emelmujiro.secreto.room.dto.request.*;
import com.emelmujiro.secreto.room.dto.response.*;

import java.util.List;

public interface RoomService {
    List<GetRoomListResponseDto> getRoomList(GetRoomListRequestDto params);

    GetRoomDetailsResponseDto getRoomDetails(GetRoomDetailsRequestDto params);

    List<GetRoomUserListResponseDto> getRoomUserList(GetRoomUserListRequestDto params);

    GetRoomUserDetailsResponseDto getRoomUserDetails(GetRoomUserDetailsRequestDto params);

    CreateRoomResponseDto createRoom(CreateRoomRequestDto params);

    UpdateRoomDetailsResponseDto updateRoomDetails(UpdateRoomDetailsRequestDto params);

    UpdateRoomStatusStartResponseDto updateRoomStatusStart(UpdateRoomStatusStartRequestDto params);

    void updateRoomStatusEnd(UpdateRoomStatusEndRequestDto params);

    EnterRoomByCodeResponseDto enterRoomByCode(EnterRoomByCodeRequestDto params);

    CreateRoomUserProfileResponseDto createRoomUserProfile(CreateRoomUserProfileRequestDto params);

    UpdateRoomUserSelfIntroductionResponseDto updateRoomUserSelfIntroduction(UpdateRoomUserSelfIntroductionRequestDto params);

    UpdateRoomUserStatusAcceptedResponseDto updateRoomUserStatusAccepted(UpdateRoomUserStatusAcceptedRequestDto params);
}
