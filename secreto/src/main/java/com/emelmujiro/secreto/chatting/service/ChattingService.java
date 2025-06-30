package com.emelmujiro.secreto.chatting.service;

import com.emelmujiro.secreto.chatting.dto.request.*;
import com.emelmujiro.secreto.chatting.dto.response.*;

import java.util.List;

public interface ChattingService {
    CreateChattingResponseDto createChatting(CreateChattingRequestDto params);

    List<GetChattingListResponseDto> getChattingList(GetChattingListRequestDto params);

    List<GetChattingParticipationListResponseDto> getChattingParticipationList(GetChattingParticipationListRequestDto params);

    List<UpdateChattingReadStatusResponseDto> updateChattingReadStatus(UpdateChattingReadStatusRequestDto params);

    GetChattingRoomDetailsResponseDto getChattingParticipationsInfo(GetChattingRoomDetailsRequestDto params);

    List<GetChattingRoomDetailsResponseDto> getChattingRoomInfoList(GetChattingRoomInfoListRequestDto params);
}
