package com.emelmujiro.secreto.game.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "character_skin_color")
public class CharacterSkinColor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "character_skin_color_id")
    private Long id;

    private String rgbCode;
}
