package com.emelmujiro.secreto.notification.service.impl;

import com.emelmujiro.secreto.notification.dto.request.*;
import com.emelmujiro.secreto.notification.dto.response.GetAllNotificationsResponseDto;
import com.emelmujiro.secreto.notification.dto.response.GetNotificationDetailsResponseDto;
import com.emelmujiro.secreto.notification.dto.response.GetRoomNotificationsResponseDto;
import com.emelmujiro.secreto.notification.dto.response.SendNotificationResponseDto;
import com.emelmujiro.secreto.notification.entity.Notification;
import com.emelmujiro.secreto.notification.repository.NotificationRepository;
import com.emelmujiro.secreto.notification.service.NotificationService;
import com.emelmujiro.secreto.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Transactional
@RequiredArgsConstructor
@Service
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;

    private final SimpMessagingTemplate messagingTemplate;

    @Transactional(readOnly = true)
    @Override
    public void sendNotification(SendNotificationRequestDto params) {

        messagingTemplate.convertAndSend(params.getNotificationType().getSubscribeUrl() + params.getTargetId(),
                SendNotificationResponseDto.builder()
                        .author(params.getAuthor())
                        .type(params.getNotificationType())
                        .content(params.getContent())
                        .build());
    }

    @Transactional(readOnly = true)
    @Override
    public void sendNotificationList(SendNotificationListRequestDto params) {

        for(Long targetId : params.getTargetIdList()) {
            messagingTemplate.convertAndSend(params.getNotificationType().getSubscribeUrl() + targetId,
                    SendNotificationResponseDto.builder()
                            .author(params.getAuthor())
                            .type(params.getNotificationType())
                            .content(params.getContent())
                            .build());
        }
    }

    @Override
    public void saveNotification(SaveNotificationRequestDto params) {

        List<Notification> newNotificationList = new ArrayList<>();
        for(User receiver : params.getReceiverList()) {
            Notification newNotification = Notification.builder()
                    .notificationType(params.getNotificationType())
                    .author(params.getAuthor())
                    .content(params.getContent())
                    .generatedDate(LocalDateTime.now())
                    .readYn(false)
                    .referenceId(params.getReferenceId())
                    .user(receiver)
                    .room(params.getRoom())
                    .build();

            newNotificationList.add(newNotification);
        }

        notificationRepository.saveAll(newNotificationList);
    }

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
    @Override
    public GetNotificationDetailsResponseDto getNotificationDetails(GetNotificationDetailsRequestDto params) {

        Notification findNotification = notificationRepository.findByIdAndUserId(params.getNotificationId(), params.getUserId());

        findNotification.readNotification();

        return GetNotificationDetailsResponseDto.from(findNotification);
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
