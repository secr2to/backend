package com.emelmujiro.secreto.notification.dto.request;

import lombok.*;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class GetNotificationDetailsRequestDto {

    private Long notificationId;
    private Long userId;
}
