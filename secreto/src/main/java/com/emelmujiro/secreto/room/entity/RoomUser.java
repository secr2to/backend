package com.emelmujiro.secreto.room.entity;

import com.emelmujiro.secreto.chatting.entity.ChattingMessage;
import com.emelmujiro.secreto.chatting.entity.ChattingParticipate;
import com.emelmujiro.secreto.game.entity.Matching;
import com.emelmujiro.secreto.game.entity.Reasoning;
import com.emelmujiro.secreto.user.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "room_user")
public class RoomUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "room_user_id")
    private Long id;

    private Boolean managerYn;

    private Boolean standbyYn;

    private String nickname;

    private Boolean useProfileYn;

    private String selfIntroduction;

    @Builder.Default
    @OneToMany(mappedBy = "roomUser", fetch = FetchType.LAZY)
    private List<ChattingParticipate> chattingParticipateList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "roomUser", fetch = FetchType.LAZY)
    private List<ChattingMessage> chattingMessageList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "roomUser", fetch = FetchType.LAZY)
    private List<Matching> matchingList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "roomUser", fetch = FetchType.LAZY)
    private List<Reasoning> reasoningList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private Room room;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(mappedBy = "roomUser", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private RoomCharacter roomCharacter;

    @OneToOne(mappedBy = "roomUser", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private RoomProfile roomProfile;

    public void setRoomProfile(RoomProfile roomProfile) {
        this.roomProfile = roomProfile;
        roomProfile.changeRoomUser(this);
    }

    public void setRoomProfile(RoomCharacter roomCharacter) {
        this.roomCharacter = roomCharacter;
        roomCharacter.changeRoomUser(this);
    }

    public void changeSelfIntroduction(String selfIntroduction) {
        this.selfIntroduction = selfIntroduction;
    }

}

