package com.emelmujiro.secreto.mission.entity;

import com.emelmujiro.secreto.room.entity.Room;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "room_mission_history")
public class RoomMissionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_mission_history_id")
    private Long id;

    private String content;

    private LocalDateTime createDate;

    private Boolean completeYn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

}
