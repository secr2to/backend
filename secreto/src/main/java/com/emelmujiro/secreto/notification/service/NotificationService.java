package com.emelmujiro.secreto.notification.service;

import com.emelmujiro.secreto.notification.dto.request.GetAllNotificationsRequestDto;
import com.emelmujiro.secreto.notification.dto.request.GetRoomNotificationsRequestDto;
import com.emelmujiro.secreto.notification.dto.request.SaveNotificationRequestDto;
import com.emelmujiro.secreto.notification.dto.request.SendNotificationRequestDto;
import com.emelmujiro.secreto.notification.dto.response.GetAllNotificationsResponseDto;
import com.emelmujiro.secreto.notification.dto.response.GetRoomNotificationsResponseDto;

public interface NotificationService {

    void sendNotification(SendNotificationRequestDto params);

    void saveNotification(SaveNotificationRequestDto params);

    GetAllNotificationsResponseDto getAllNotifications(GetAllNotificationsRequestDto params);

    GetRoomNotificationsResponseDto getRoomNotifications(GetRoomNotificationsRequestDto params);
}
