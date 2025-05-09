package com.emelmujiro.secreto.chatting.service;

import com.emelmujiro.secreto.chatting.dto.CreateChattingReqDto;
import com.emelmujiro.secreto.chatting.dto.CreateChattingResDto;
import com.emelmujiro.secreto.chatting.dto.GetChattingListReqDto;
import com.emelmujiro.secreto.chatting.dto.GetChattingListResDto;

import java.util.List;

public interface ChattingService {
    CreateChattingResDto createChatting(CreateChattingReqDto createChattingReqDto);

    List<GetChattingListResDto> getChattingList(GetChattingListReqDto getChattingListReqDto);
}
