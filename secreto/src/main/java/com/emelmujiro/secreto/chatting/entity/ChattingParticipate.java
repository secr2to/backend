package com.emelmujiro.secreto.chatting.entity;

import com.emelmujiro.secreto.room.entity.RoomUser;
import jakarta.persistence.*;
import lombok.*;

@Builder
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "chatting_participate")
public class ChattingParticipate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatting_participate_id")
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, name = "type")
    private ChattingParticipateType chattingUserType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatting_room_id")
    private ChattingRoom chattingRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_user_id")
    private RoomUser roomUser;

}



