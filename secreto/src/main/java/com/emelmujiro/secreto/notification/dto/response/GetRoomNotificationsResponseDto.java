package com.emelmujiro.secreto.notification.dto.response;

import com.emelmujiro.secreto.notification.entity.Notification;
import lombok.*;
import org.springframework.data.domain.Page;

import java.util.List;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class GetRoomNotificationsResponseDto {

    List<NotificationDto> notificationList;
    private Integer totalPages;
    private Long totalNotifications;

    public static GetRoomNotificationsResponseDto from(Page<Notification> pagedNotificationList) {

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

        return GetRoomNotificationsResponseDto.builder()
                .notificationList(notificationList)
                .totalPages(pagedNotificationList.getTotalPages())
                .totalNotifications(pagedNotificationList.getTotalElements())
                .build();
    }
}
