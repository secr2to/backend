package com.emelmujiro.secreto.game.entity;

import com.emelmujiro.secreto.room.entity.RoomUser;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "reasoning")
public class Reasoning {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reasoning_id")
    private Long id;

    private Long reasoningManitoId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_user_id")
    private RoomUser roomUser;
}
