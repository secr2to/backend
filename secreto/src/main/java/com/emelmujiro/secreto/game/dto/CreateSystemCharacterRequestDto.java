package com.emelmujiro.secreto.game.dto;

import lombok.*;

@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CreateSystemCharacterRequestDto {

    private String clothesColor;
    private String skinColor;
    private String url;
}
