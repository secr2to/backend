package com.emelmujiro.secreto.notification.service;

import com.emelmujiro.secreto.notification.dto.request.GetAllNotificationsRequestDto;
import com.emelmujiro.secreto.notification.dto.request.GetRoomNotificationsRequestDto;
import com.emelmujiro.secreto.notification.dto.request.SendAndSaveNotificationRequestDto;
import com.emelmujiro.secreto.notification.dto.request.SendNotificationRequestDto;
import com.emelmujiro.secreto.notification.dto.response.GetAllNotificationsResponseDto;
import com.emelmujiro.secreto.notification.dto.response.GetRoomNotificationsResponseDto;
import com.emelmujiro.secreto.notification.entity.NotificationType;

public interface NotificationService {

    void sendNotification(SendNotificationRequestDto params);

    void sendAndSaveNotification(SendAndSaveNotificationRequestDto params);

    GetAllNotificationsResponseDto getAllNotifications(GetAllNotificationsRequestDto params);

    GetRoomNotificationsResponseDto getRoomNotifications(GetRoomNotificationsRequestDto params);
}
