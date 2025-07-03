package com.emelmujiro.secreto.notification.dto.request;

import com.emelmujiro.secreto.auth.annotation.LoginUser;
import com.emelmujiro.secreto.global.annotation.InjectPathVariable;
import lombok.*;
import org.springframework.data.domain.Pageable;

@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class GetRoomNotificationsRequestDto {

    @InjectPathVariable(name = "roomId")
    private Long roomId;

    private Pageable pageable;
    private NotificationSearchPeriod period;
    private Boolean readYn;

    @LoginUser
    private Long userId;
}
