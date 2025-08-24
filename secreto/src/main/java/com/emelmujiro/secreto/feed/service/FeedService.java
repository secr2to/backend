package com.emelmujiro.secreto.feed.service;

import com.emelmujiro.secreto.feed.dto.request.CreateFeedRequestDto;
import com.emelmujiro.secreto.feed.dto.request.DeleteFeedRequestDto;
import com.emelmujiro.secreto.feed.dto.request.GetFeedRequestDto;
import com.emelmujiro.secreto.feed.dto.request.GetIngameFeedsRequestDto;
import com.emelmujiro.secreto.feed.dto.request.HeartRequestDto;
import com.emelmujiro.secreto.feed.dto.request.UpdateFeedRequestDto;
import com.emelmujiro.secreto.feed.dto.response.CreateFeedResponseDto;
import com.emelmujiro.secreto.feed.dto.response.GetFeedResponseDto;
import com.emelmujiro.secreto.feed.dto.response.GetIngameFeedsResponseDto;
import com.emelmujiro.secreto.global.dto.response.SuccessResponseDto;

public interface FeedService {

	GetIngameFeedsResponseDto getIngameFeeds(GetIngameFeedsRequestDto dto);

	CreateFeedResponseDto create(CreateFeedRequestDto dto);

	GetFeedResponseDto find(GetFeedRequestDto dto);

	SuccessResponseDto update(UpdateFeedRequestDto dto);

	SuccessResponseDto delete(DeleteFeedRequestDto dto);

	SuccessResponseDto heart(HeartRequestDto dto);

	SuccessResponseDto unheart(HeartRequestDto dto);
}
