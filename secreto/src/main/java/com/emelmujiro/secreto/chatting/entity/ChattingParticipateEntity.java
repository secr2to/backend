package com.emelmujiro.secreto.chatting.entity;

import com.emelmujiro.secreto.room.entity.RoomUserEntity;
import jakarta.persistence.*;
import lombok.*;

@Builder
@AllArgsConstructor
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "chatting_participate")
public class ChattingParticipateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chatting_participate_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatting_room_id")
    private ChattingRoomEntity chattingRoomEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_user_id")
    private RoomUserEntity roomUserEntity;

    @Enumerated(value = EnumType.STRING)
    @Column(nullable = false)
    private ChattingParticipateType chattingUserType;
}

