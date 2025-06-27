package com.emelmujiro.secreto.room.dto.request;

import com.emelmujiro.secreto.auth.annotation.LoginUser;
import com.emelmujiro.secreto.global.annotation.InjectPathVariable;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UpdateRoomImageRequestDto {

    private MultipartFile roomImage;

    @InjectPathVariable(name = "roomId")
    private Long roomId;

    @LoginUser
    private Long userId;
}
