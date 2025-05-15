package com.emelmujiro.secreto.room.dto.response;

import lombok.*;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class GetRoomUserListResDto {

    private long roomUserId;
    private boolean managerYn;
    private boolean standbyYn;
    private String nickname;
    private boolean useProfileYn;
    private String selfIntroduction;
    private String profileUrl;
    private String skinColorRGB;
    private String clothesColorRGB;
}
