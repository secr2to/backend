package com.emelmujiro.secreto.room.dto.request;

import com.emelmujiro.secreto.auth.annotation.LoginUser;
import com.emelmujiro.secreto.global.annotation.InjectPathVariable;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CreateRoomUserProfileRequestDto {

    private String nickname;
    private Boolean useProfileYn;
    private String selfIntroduction;
    private MultipartFile profileImage;
    private String skinRgbCode;
    private String ClothesRgbCode;

    @InjectPathVariable(name = "roomId")
    private Long roomId;

    @LoginUser
    private Long userId;
}
