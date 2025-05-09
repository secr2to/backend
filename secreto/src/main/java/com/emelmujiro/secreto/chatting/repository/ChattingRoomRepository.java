package com.emelmujiro.secreto.chatting.repository;

import com.emelmujiro.secreto.chatting.entity.ChattingRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChattingRoomRepository extends JpaRepository<ChattingRoom, Long> {
}
