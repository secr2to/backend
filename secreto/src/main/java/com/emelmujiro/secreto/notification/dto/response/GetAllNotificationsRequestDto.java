package com.emelmujiro.secreto.notification.dto.response;

import com.emelmujiro.secreto.mission.dto.request.NotificationSearchPeriod;
import lombok.*;
import org.springframework.data.domain.Pageable;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class GetAllNotificationsRequestDto {

    private Pageable pageable;
    private NotificationSearchPeriod period;
    private Boolean readYn;
    private Long userId;
}
