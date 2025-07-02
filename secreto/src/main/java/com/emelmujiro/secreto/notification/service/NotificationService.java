package com.emelmujiro.secreto.notification.service;

import com.emelmujiro.secreto.notification.dto.request.GetAllNotificationsRequestDto;
import com.emelmujiro.secreto.notification.dto.request.GetRoomNotificationsRequestDto;
import com.emelmujiro.secreto.notification.dto.response.GetAllNotificationsResponseDto;
import com.emelmujiro.secreto.notification.dto.response.GetRoomNotificationsResponseDto;

public interface NotificationService {
    GetAllNotificationsResponseDto getAllNotifications(GetAllNotificationsRequestDto params);

    GetRoomNotificationsResponseDto getRoomNotifications(GetRoomNotificationsRequestDto params);
}
