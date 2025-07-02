package com.emelmujiro.secreto.notification.service.impl;

import com.emelmujiro.secreto.mission.dto.request.NotificationSearchPeriod;
import com.emelmujiro.secreto.notification.dto.GetAllNotificationsRequestDto;
import com.emelmujiro.secreto.notification.dto.GetAllNotificationsResponseDto;
import com.emelmujiro.secreto.notification.entity.Notification;
import com.emelmujiro.secreto.notification.repository.NotificationRepository;
import com.emelmujiro.secreto.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    @Override
    public GetAllNotificationsResponseDto getAllNotifications(GetAllNotificationsRequestDto params) {

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate = null;
        LocalDateTime endDate = null;

        switch (params.getPeriod()) {
            case TODAY:
                startDate = now.minusHours(24);
                endDate = now;
                break;

            case WEEK:
                startDate = now.minusDays(7);
                endDate = now;
                break;

            case ALL:

            default:
                break;
        }

        Page<Notification> notificationList;
        if(params.getPeriod() == NotificationSearchPeriod.ALL) {

            if(params.getReadYn() == null) {
                notificationList = notificationRepository.findAllByUserId(params.getUserId(), params.getPageable());
            }
            else {
                notificationList = notificationRepository.findAllByUserIdAndReadYn(params.getUserId(), params.getReadYn(), params.getPageable());
            }
        }
        else {

            if(params.getReadYn() == null) {
                notificationList = notificationRepository.findAllByUserIdAndStartDateAndEndDate(params.getUserId(), startDate, endDate, params.getPageable());
            }
            else {
                notificationList = notificationRepository.findAllByUserIdAndStartDateAndEndDateAndReadYn(params.getUserId(), startDate, endDate, params.getReadYn(), params.getPageable());
            }
        }

        return GetAllNotificationsResponseDto.from(notificationList);
    }
}
