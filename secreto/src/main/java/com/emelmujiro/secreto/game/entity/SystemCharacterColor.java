package com.emelmujiro.secreto.game.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "system_character_color")
public class SystemCharacterColor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "system_character_color_id")
    private Long id;

    private String clothesRgbCode;

    private String skinRgbCode;

    private String url;
}
