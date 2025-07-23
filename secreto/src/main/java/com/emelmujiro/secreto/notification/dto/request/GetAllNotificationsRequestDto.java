package com.emelmujiro.secreto.notification.dto.request;

import com.emelmujiro.secreto.auth.annotation.LoginUser;
import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class GetAllNotificationsRequestDto {

    private Integer page = 0;
    private Integer size = 10;
    private NotificationSearchPeriod period;
    private Boolean readYn;

    @LoginUser
    private Long userId;

    public Pageable getPageable() {
        return PageRequest.of(page, size);
    }
}
