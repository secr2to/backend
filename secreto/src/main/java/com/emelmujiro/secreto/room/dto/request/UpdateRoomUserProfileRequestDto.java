package com.emelmujiro.secreto.room.dto.request;

import com.emelmujiro.secreto.auth.annotation.InjectUserId;
import com.emelmujiro.secreto.global.annotation.InjectPathVariable;
import lombok.*;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UpdateRoomUserProfileRequestDto {

    private String nickname;
    private Boolean useProfileYn;
    private String selfIntroduction;
    private String profileUrl;
    private String skinColorRgb;
    private String clothesColorRgb;

    @InjectPathVariable(name = "roomId")
    private Long roomId;

    @InjectUserId
    private Long userId;
}
