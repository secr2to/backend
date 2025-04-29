package com.emelmujiro.secreto.room.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "room_character")
public class RoomCharacter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_character_id")
    private Long id;

    private Integer skinColorId;

    private Integer clothesColorId;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_user_id")
    private RoomUser roomUser;
}
