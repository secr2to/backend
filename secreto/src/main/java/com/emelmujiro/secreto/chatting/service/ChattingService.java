package com.emelmujiro.secreto.chatting.service;

import com.emelmujiro.secreto.chatting.dto.request.CreateChattingReqDto;
import com.emelmujiro.secreto.chatting.dto.request.GetChattingParticipationListReqDto;
import com.emelmujiro.secreto.chatting.dto.request.UpdateChattingReadStatusReqDto;
import com.emelmujiro.secreto.chatting.dto.response.CreateChattingResDto;
import com.emelmujiro.secreto.chatting.dto.request.GetChattingListReqDto;
import com.emelmujiro.secreto.chatting.dto.response.GetChattingListResDto;
import com.emelmujiro.secreto.chatting.dto.response.GetChattingParticipationListResDto;
import com.emelmujiro.secreto.chatting.dto.response.UpdateChattingReadStatusResDto;

import java.util.List;

public interface ChattingService {
    CreateChattingResDto createChatting(CreateChattingReqDto params);

    List<GetChattingListResDto> getChattingList(GetChattingListReqDto params);

    List<GetChattingParticipationListResDto> getChattingParticipationList(GetChattingParticipationListReqDto params);

    List<UpdateChattingReadStatusResDto> updateChattingReadStatus(UpdateChattingReadStatusReqDto params);
}
