package com.emelmujiro.secreto.notification.dto.request;

import com.emelmujiro.secreto.notification.entity.NotificationType;
import lombok.*;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class SendNotificationRequestDto {

    private NotificationType notificationType;
    private String content;
    private String author;
    private Long targetId;
}
