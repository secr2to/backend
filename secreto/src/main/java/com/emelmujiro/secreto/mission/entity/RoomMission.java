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
@Table(name = "room_mission")
public class RoomMission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_mission_id")
    private Long id;

    private String content;

    private Boolean executeYn;

    private LocalDateTime executedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    public void executeMission() {
        this.executeYn = true;
        this.executedDate = LocalDateTime.now();
    }

}
