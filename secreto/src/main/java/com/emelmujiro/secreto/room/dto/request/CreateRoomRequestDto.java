package com.emelmujiro.secreto.room.dto.request;

import com.emelmujiro.secreto.auth.annotation.LoginUser;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@ToString
@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor
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

    @LoginUser
    private Long managerId;
}
