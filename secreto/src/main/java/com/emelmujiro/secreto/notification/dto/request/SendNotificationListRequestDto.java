package com.emelmujiro.secreto.notification.dto.request;

import com.emelmujiro.secreto.notification.entity.NotificationType;
import com.emelmujiro.secreto.room.entity.Room;
import com.emelmujiro.secreto.user.entity.User;
import lombok.*;

import java.util.List;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class SendNotificationListRequestDto {

    private NotificationType notificationType;
    private String content;
    private String author;
    private List<Long> targetIdList;
}
