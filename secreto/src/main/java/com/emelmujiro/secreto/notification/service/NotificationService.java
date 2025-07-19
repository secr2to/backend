package com.emelmujiro.secreto.notification.service;

import com.emelmujiro.secreto.notification.dto.request.SendNotificationListRequestDto;
import com.emelmujiro.secreto.notification.dto.request.*;
import com.emelmujiro.secreto.notification.dto.response.GetAllNotificationsResponseDto;
import com.emelmujiro.secreto.notification.dto.response.GetNotificationDetailsResponseDto;
import com.emelmujiro.secreto.notification.dto.response.GetRoomNotificationsResponseDto;

public interface NotificationService {

    void sendNotification(SendNotificationRequestDto params);

    void sendNotificationList(SendNotificationListRequestDto params);

    void saveNotification(SaveNotificationRequestDto params);

    GetAllNotificationsResponseDto getAllNotifications(GetAllNotificationsRequestDto params);

    GetRoomNotificationsResponseDto getRoomNotifications(GetRoomNotificationsRequestDto params);

    GetNotificationDetailsResponseDto getNotificationDetails(GetNotificationDetailsRequestDto params);
}
