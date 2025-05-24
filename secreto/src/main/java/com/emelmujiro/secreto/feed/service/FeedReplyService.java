package com.emelmujiro.secreto.feed.service;

import java.util.Map;

import com.emelmujiro.secreto.feed.dto.request.DeleteReplyRequestDto;
import com.emelmujiro.secreto.feed.dto.request.GetRepliesRequestDto;
import com.emelmujiro.secreto.feed.dto.request.ReplyHeartRequestDto;
import com.emelmujiro.secreto.feed.dto.request.UpdateReplyRequestDto;
import com.emelmujiro.secreto.feed.dto.request.WriteReplyRequestDto;
import com.emelmujiro.secreto.feed.dto.response.GetRepliesResponseDto;
import com.emelmujiro.secreto.feed.dto.response.WriteReplyResponseDto;
import com.emelmujiro.secreto.global.dto.response.SuccessResponseDto;

public interface FeedReplyService {

	GetRepliesResponseDto getReplies(GetRepliesRequestDto dto);

	WriteReplyResponseDto writeReply(WriteReplyRequestDto dto);

	SuccessResponseDto updateReply(UpdateReplyRequestDto dto);

	SuccessResponseDto deleteReply(DeleteReplyRequestDto dto);

	SuccessResponseDto replyHeart(ReplyHeartRequestDto dto);

	SuccessResponseDto replyUnheart(ReplyHeartRequestDto dto);

}
