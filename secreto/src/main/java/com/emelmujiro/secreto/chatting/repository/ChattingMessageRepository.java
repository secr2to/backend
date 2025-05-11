package com.emelmujiro.secreto.chatting.repository;

import com.emelmujiro.secreto.chatting.entity.ChattingMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ChattingMessageRepository extends JpaRepository<ChattingMessage, Long> {

    @Query("select cm from ChattingMessage cm where cm.chattingRoom.id = :chattingRoomId")
    List<ChattingMessage> findAllByChattingRoomId(@Param("chattingRoomId") Long chattingRoomId);

    @Query("select cm from ChattingMessage cm where cm.chattingRoom.id = :chattingRoomId and cm.id in :chattingMessageIds")
    List<ChattingMessage> findAllByChattingRoomIdAndChattingMessageId(@Param("chattingRoomId") Long chattingRoomId, @Param("chattingMessageIds") List<Long> chattingMessageIds);
}
