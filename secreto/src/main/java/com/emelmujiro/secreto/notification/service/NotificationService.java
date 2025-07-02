package com.emelmujiro.secreto.notification.service;

import com.emelmujiro.secreto.notification.dto.GetAllNotificationsRequestDto;
import com.emelmujiro.secreto.notification.dto.GetAllNotificationsResponseDto;

import java.util.List;

public interface NotificationService {
    GetAllNotificationsResponseDto getAllNotifications(GetAllNotificationsRequestDto params);
}
