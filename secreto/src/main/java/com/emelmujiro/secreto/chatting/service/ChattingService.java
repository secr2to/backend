package com.emelmujiro.secreto.chatting.service;

import com.emelmujiro.secreto.chatting.dto.CreateChattingReqDto;
import com.emelmujiro.secreto.chatting.dto.CreateChattingResDto;

public interface ChattingService {
    CreateChattingResDto createChatting(CreateChattingReqDto createChattingReqDto);
}
