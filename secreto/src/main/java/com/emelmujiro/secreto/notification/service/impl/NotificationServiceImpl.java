package com.emelmujiro.secreto.notification.service.impl;

import com.emelmujiro.secreto.notification.dto.request.NotificationSearchPeriod;
import com.emelmujiro.secreto.notification.dto.request.GetAllNotificationsRequestDto;
import com.emelmujiro.secreto.notification.dto.request.GetRoomNotificationsRequestDto;
import com.emelmujiro.secreto.notification.dto.response.GetAllNotificationsResponseDto;
import com.emelmujiro.secreto.notification.dto.response.GetRoomNotificationsResponseDto;
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

        DateInfo dateInfo = getStartDateAndEndDate(params.getPeriod());

        Page<Notification> notificationList;
        if(params.getPeriod() == NotificationSearchPeriod.ALL) {

            if(params.getReadYn() == null) {
                notificationList = notificationRepository.findAllByUserId(params.getUserId(),
                        params.getPageable());
            }
            else {
                notificationList = notificationRepository.findAllByUserIdAndReadYn(params.getUserId(),
                        params.getReadYn(), params.getPageable());
            }
        }
        else {

            if(params.getReadYn() == null) {
                notificationList = notificationRepository.findAllByUserIdAndStartDateAndEndDate(params.getUserId(),
                        dateInfo.startDate(), dateInfo.endDate(), params.getPageable());
            }
            else {
                notificationList = notificationRepository.findAllByUserIdAndStartDateAndEndDateAndReadYn(params.getUserId(),
                        dateInfo.startDate(), dateInfo.endDate(), params.getReadYn(), params.getPageable());
            }
        }

        return GetAllNotificationsResponseDto.from(notificationList);
    }

    @Override
    public GetRoomNotificationsResponseDto getRoomNotifications(GetRoomNotificationsRequestDto params) {

        DateInfo dateInfo = getStartDateAndEndDate(params.getPeriod());

        Page<Notification> notificationList;
        if(params.getPeriod() == NotificationSearchPeriod.ALL) {

            if(params.getReadYn() == null) {
                notificationList = notificationRepository.findAllByUserIdAndRoomId(params.getUserId(),
                        params.getRoomId(), params.getPageable());
            }
            else {
                notificationList = notificationRepository.findAllByUserIdAndRoomIdAndReadYn(params.getUserId(),
                        params.getRoomId(), params.getReadYn(), params.getPageable());
            }
        }
        else {

            if(params.getReadYn() == null) {
                notificationList = notificationRepository.findAllByUserIdAndRoomIdAndStartDateAndEndDate(params.getUserId(),
                        params.getRoomId(), dateInfo.startDate(), dateInfo.endDate(), params.getPageable());
            }
            else {
                notificationList = notificationRepository.findAllByUserIdAndRoomIdAndStartDateAndEndDateAndReadYn(params.getUserId(),
                        params.getRoomId(), dateInfo.startDate(), dateInfo.endDate(), params.getReadYn(), params.getPageable());
            }
        }

        return GetRoomNotificationsResponseDto.from(notificationList);
    }

    private static DateInfo getStartDateAndEndDate(NotificationSearchPeriod period) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startDate = null;
        LocalDateTime endDate = null;

        switch (period) {
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

        return new DateInfo(startDate, endDate);
    }

    private record DateInfo(LocalDateTime startDate, LocalDateTime endDate) {}
}
