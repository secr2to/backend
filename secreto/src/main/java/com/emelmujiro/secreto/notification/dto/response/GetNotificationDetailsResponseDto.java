package com.emelmujiro.secreto.notification.dto.response;

import com.emelmujiro.secreto.notification.entity.Notification;
import com.emelmujiro.secreto.notification.entity.NotificationType;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class GetNotificationDetailsResponseDto {

    private Long notificationId;
    private String author;
    private String content;
    private LocalDateTime generatedDate;
    private Boolean readYn;
    private NotificationType type;
    private Long referenceId;

    public static GetNotificationDetailsResponseDto from(Notification notification) {

        return GetNotificationDetailsResponseDto.builder()
                .notificationId(notification.getId())
                .author(notification.getAuthor())
                .content(notification.getContent())
                .generatedDate(LocalDateTime.now())
                .readYn(notification.getReadYn())
                .type(notification.getNotificationType())
                .referenceId(notification.getReferenceId())
                .build();
    }
}
