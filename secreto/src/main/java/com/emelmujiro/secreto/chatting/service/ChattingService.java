package com.emelmujiro.secreto.chatting.service;

import com.emelmujiro.secreto.chatting.dto.*;

import java.util.List;

public interface ChattingService {
    CreateChattingResDto createChatting(CreateChattingReqDto params);

    List<GetChattingListResDto> getChattingList(GetChattingListReqDto params);

    List<GetChattingParticipationListResDto> getChattingParticipationList(GetChattingParticipationListReqDto params);

    List<UpdateChattingReadStatusResDto> updateChattingReadStatus(UpdateChattingReadStatusReqDto params);
}
