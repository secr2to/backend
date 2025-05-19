package com.emelmujiro.secreto.chatting.service;

import com.emelmujiro.secreto.chatting.dto.request.CreateChattingRequestDto;
import com.emelmujiro.secreto.chatting.dto.request.GetChattingParticipationListRequestDto;
import com.emelmujiro.secreto.chatting.dto.request.UpdateChattingReadStatusRequestDto;
import com.emelmujiro.secreto.chatting.dto.response.CreateChattingResponseDto;
import com.emelmujiro.secreto.chatting.dto.request.GetChattingListRequestDto;
import com.emelmujiro.secreto.chatting.dto.response.GetChattingListResponseDto;
import com.emelmujiro.secreto.chatting.dto.response.GetChattingParticipationListResponseDto;
import com.emelmujiro.secreto.chatting.dto.response.UpdateChattingReadStatusResponseDto;

import java.util.List;

public interface ChattingService {
    CreateChattingResponseDto createChatting(CreateChattingRequestDto params);

    List<GetChattingListResponseDto> getChattingList(GetChattingListRequestDto params);

    List<GetChattingParticipationListResponseDto> getChattingParticipationList(GetChattingParticipationListRequestDto params);

    List<UpdateChattingReadStatusResponseDto> updateChattingReadStatus(UpdateChattingReadStatusRequestDto params);
}
