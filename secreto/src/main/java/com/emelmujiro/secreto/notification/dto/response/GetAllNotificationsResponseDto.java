package com.emelmujiro.secreto.notification.dto.response;

import com.emelmujiro.secreto.notification.entity.Notification;
import lombok.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class GetAllNotificationsResponseDto {

    List<NotificationDto> notificationList;
    private Integer totalPages;
    private Long totalNotifications;
    private Long offset;
    private Boolean hasNext;

    public static GetAllNotificationsResponseDto from(Page<Notification> pagedNotificationList) {

        List<NotificationDto> notificationList = pagedNotificationList.stream()
                .map(notification -> NotificationDto.builder()
                        .notificationId(notification.getId())
                        .author(notification.getAuthor())
                        .content(notification.getContent())
                        .generatedDate(notification.getGeneratedDate())
                        .readYn(notification.getReadYn())
                        .type(notification.getNotificationType())
                        .referenceId(notification.getReferenceId())
                        .build()
                )
                .toList();

        return GetAllNotificationsResponseDto.builder()
                .notificationList(notificationList)
                .totalPages(pagedNotificationList.getTotalPages())
                .totalNotifications(pagedNotificationList.getTotalElements())
                .offset(pagedNotificationList.getPageable().getOffset())
                .hasNext(pagedNotificationList.hasNext())
                .build();
    }
}
