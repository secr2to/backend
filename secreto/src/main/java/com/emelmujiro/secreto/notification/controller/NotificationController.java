package com.emelmujiro.secreto.notification.controller;

import com.emelmujiro.secreto.global.response.ApiResponse;
import com.emelmujiro.secreto.notification.dto.request.NotificationSearchPeriod;
import com.emelmujiro.secreto.notification.dto.request.GetAllNotificationsRequestDto;
import com.emelmujiro.secreto.notification.dto.request.GetRoomNotificationsRequestDto;
import com.emelmujiro.secreto.notification.dto.response.GetAllNotificationsResponseDto;
import com.emelmujiro.secreto.notification.dto.response.GetRoomNotificationsResponseDto;
import com.emelmujiro.secreto.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class NotificationController {

    private final NotificationService notificationService;

    /*
    * 전체 알림 내용 조회
    * */
    @GetMapping("/notifications")
    public ResponseEntity<ApiResponse<Object>> getAllNotifications(@ModelAttribute GetAllNotificationsRequestDto params) {

        NotificationSearchPeriod.checkValidation(params.getPeriod());

        GetAllNotificationsResponseDto result = notificationService.getAllNotifications(params);

        return ApiResponse.builder()
                .data(result)
                .status(HttpStatus.OK)
                .message("전체 알림 리스트를 조회하였습니다.")
                .success();
    }

    /*
    * 특정 방 알림 내용 조회
    * */
    @GetMapping("/rooms/{roomId}/notifications")
    public ResponseEntity<ApiResponse<Object>> getRoomNotifications(@ModelAttribute GetRoomNotificationsRequestDto params) {

        NotificationSearchPeriod.checkValidation(params.getPeriod());

        GetRoomNotificationsResponseDto result = notificationService.getRoomNotifications(params);

        return ApiResponse.builder()
                .data(result)
                .status(HttpStatus.OK)
                .message("방의 알림 리스트를 조회하였습니다.")
                .success();
    }

}
