package com.emelmujiro.secreto.room.entity;

import com.emelmujiro.secreto.feed.entity.Feed;
import com.emelmujiro.secreto.mission.entity.RoomMission;
import com.emelmujiro.secreto.mission.entity.RoomMissionHistory;
import com.emelmujiro.secreto.notification.entity.Notification;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "room")
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_id")
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "status")
    private RoomStatus roomStatus;

    private String code;

    private LocalDateTime startDate;

    private LocalDateTime endDate;

    private Integer missionPeriod;

    @Builder.Default
    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
    private List<RoomUser> roomUserList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
    private List<RoomMissionHistory> roomMissionHistoryList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
    private List<RoomMission> roomMissionList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
    private List<Feed> feedList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY)
    private List<Notification> notificationList = new ArrayList<>();
}
