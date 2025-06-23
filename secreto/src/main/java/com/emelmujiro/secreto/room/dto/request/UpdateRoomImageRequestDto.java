package com.emelmujiro.secreto.room.dto.request;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@Setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UpdateRoomImageRequestDto {

    private MultipartFile roomImage;
}
