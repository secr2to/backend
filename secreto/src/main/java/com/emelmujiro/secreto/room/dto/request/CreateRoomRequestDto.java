package com.emelmujiro.secreto.room.dto.request;

import com.emelmujiro.secreto.auth.annotation.InjectUserId;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CreateRoomRequestDto {

    private String name;
    private LocalDateTime endDate;
    private int missionPeriod;
    private Boolean useProfileYn;
    private String nickname;
    private String selfIntroduction;
    private String clothesRgbCode;
    private String skinRgbCode;
    private MultipartFile profileImage;

    @InjectUserId
    private Long managerId;
}
