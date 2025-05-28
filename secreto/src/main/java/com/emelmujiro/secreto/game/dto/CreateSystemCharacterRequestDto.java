package com.emelmujiro.secreto.game.dto;

import lombok.*;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CreateSystemCharacterRequestDto {

    private String clothesRgbCode;
    private String skinRgbCode;
    private String url;
}
