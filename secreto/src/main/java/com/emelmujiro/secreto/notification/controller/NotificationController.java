package com.emelmujiro.secreto.notification.controller;

import com.emelmujiro.secreto.auth.annotation.LoginUser;
import com.emelmujiro.secreto.chatting.entity.ChattingParticipateType;
import com.emelmujiro.secreto.global.response.ApiResponse;
import com.emelmujiro.secreto.mission.dto.request.NotificationSearchPeriod;
import com.emelmujiro.secreto.notification.dto.GetAllNotificationsRequestDto;
import com.emelmujiro.secreto.notification.dto.GetAllNotificationsResponseDto;
import com.emelmujiro.secreto.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class NotificationController {

    private final NotificationService notificationService;

    @GetMapping("/notifications")
    public ResponseEntity<ApiResponse<Object>> getAllNotifications(@RequestParam(name = "period", required = false, defaultValue = "ALL") String period,
                                                                   @RequestParam(name = "readYn", required = false) Boolean readYn,
                                                                   Pageable pageable,
                                                                   @LoginUser Long userId) {

        NotificationSearchPeriod notificationSearchPeriod;
        try {
            notificationSearchPeriod = NotificationSearchPeriod.valueOf(period);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("잘못된 요청입니다. (TODAY, WEEK, ALL)");
        }

        GetAllNotificationsRequestDto params = GetAllNotificationsRequestDto.builder()
                .pageable(pageable)
                .period(notificationSearchPeriod)
                .readYn(readYn)
                .userId(userId)
                .build();

        GetAllNotificationsResponseDto result = notificationService.getAllNotifications(params);

        return ApiResponse.builder()
                .data(result)
                .status(HttpStatus.OK)
                .message("알림 리스트를 조회하였습니다.")
                .success();
    }
}
