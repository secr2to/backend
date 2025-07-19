package com.emelmujiro.secreto.notification.dto.response;

import com.emelmujiro.secreto.notification.entity.NotificationType;
import lombok.*;

@ToString
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class SendNotificationResponseDto {
    private String author;
    private String content;
    private NotificationType type;
}
