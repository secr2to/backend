package com.emelmujiro.secreto.room.entity;

import com.emelmujiro.secreto.feed.entity.Feed;
import com.emelmujiro.secreto.mission.entity.RoomMission;
import com.emelmujiro.secreto.mission.entity.RoomMissionHistory;
import com.emelmujiro.secreto.notification.entity.Notification;
import com.emelmujiro.secreto.room.dto.request.CreateRoomRequestDto;
import com.emelmujiro.secreto.room.dto.request.CreateRoomUserProfileRequestDto;
import com.emelmujiro.secreto.room.error.RoomErrorCode;
import com.emelmujiro.secreto.room.exception.RoomException;
import com.emelmujiro.secreto.user.entity.User;
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

    private String imageUrl;

    @Builder.Default
    @OneToMany(mappedBy = "room", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
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

    public RoomUser addManagerUser(User user, CreateRoomRequestDto params) {

        RoomUser newRoomUser = RoomUser.builder()
                .useProfileYn(params.getUseProfileYn())
                .managerYn(true)
                .standbyYn(false)
                .room(this)
                .user(user)
                .selfIntroduction(params.getSelfIntroduction())
                .nickname(params.getNickname())
                .build();

        this.roomUserList.add(newRoomUser);

        return newRoomUser;
    }

    public RoomUser addRoomUser(User user, CreateRoomUserProfileRequestDto params) {

        RoomUser newRoomUser = RoomUser.builder()
                .managerYn(false)
                .standbyYn(true)
                .room(this)
                .user(user)
                .nickname(params.getNickname())
                .selfIntroduction(params.getSelfIntroduction())
                .useProfileYn(params.getUseProfileYn())
                .build();

        return newRoomUser;
    }

    public void updateRoomInfo(LocalDateTime endDate, Integer missionPeriod) {

        if(endDate != null) {
            this.endDate = endDate;
        }
        if(missionPeriod != null) {
            this.missionPeriod = missionPeriod;
        }
    }

    public void startRoom() {

        if(this.roomStatus == RoomStatus.PROGRESS || this.roomStatus == RoomStatus.TERMINATED) {
            throw new RoomException(RoomErrorCode.ALREADY_STARTED_OR_TERMINATED);
        }
        this.roomStatus = RoomStatus.PROGRESS;
        this.startDate = LocalDateTime.now();
    }

    public void terminateRoom() {

        this.roomStatus = RoomStatus.TERMINATED;
        this.endDate = LocalDateTime.now();
    }
}
