package com.emelmujiro.secreto.notification.dto.response;

import com.emelmujiro.secreto.notification.entity.NotificationType;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class NotificationDto {
    private String author;
    private String content;
    private LocalDateTime generatedDate;
    private Boolean readYn;
    private NotificationType type;
    private Long referenceId;
}
