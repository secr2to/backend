package com.emelmujiro.secreto.notification.dto;

import com.emelmujiro.secreto.notification.entity.Notification;
import com.emelmujiro.secreto.notification.entity.NotificationType;
import lombok.*;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class GetAllNotificationsResponseDto {

    List<NotificationDto> notificationList;
    private Integer totalPages;
    private Long totalNotifications;

    public static GetAllNotificationsResponseDto from(Page<Notification> pagedNotificationList) {

        List<NotificationDto> notificationList = pagedNotificationList.stream()
                .map(notification -> NotificationDto.builder()
                        .author(notification.getAuthor())
                        .content(notification.getContent())
                        .generatedDate(notification.getGeneratedDate())
                        .readYn(notification.getReadYn())
                        .type(notification.getNotificationType())
                        .build()
                )
                .toList();

        return GetAllNotificationsResponseDto.builder()
                .notificationList(notificationList)
                .totalPages(pagedNotificationList.getTotalPages())
                .totalNotifications(pagedNotificationList.getTotalElements())
                .build();
    }
}
