package com.emelmujiro.secreto.room.entity;

import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "room_profile")
public class RoomProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_profile_id")
    private Long id;

    private String url;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_user_id")
    private RoomUser roomUser;

}
