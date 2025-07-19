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
public class SaveNotificationRequestDto {

    private NotificationType notificationType;
    private String author;
    private String content;
    private Long targetId;
    private List<User> receiverList;
    private Room room;
    private Long referenceId;
}
