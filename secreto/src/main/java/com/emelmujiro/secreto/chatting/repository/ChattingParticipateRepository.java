package com.emelmujiro.secreto.chatting.repository;

import com.emelmujiro.secreto.chatting.entity.ChattingParticipate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ChattingParticipateRepository extends JpaRepository<ChattingParticipate, Long> {

    @Query("select cp from ChattingParticipate cp where cp.roomUser.id = :roomUserId and cp.chattingUserType = :type")
    Optional<ChattingParticipate> findByRoomUserIdAndType(@Param("roomUserId") Long roomUserId, @Param("type") String type);

    @Query("select cp from ChattingParticipate cp join fetch cp.chattingRoom cr where cp.roomUser.id = :roomUserId")
    List<ChattingParticipate> findAllWithChattingRoomByRoomUserId(@Param("roomUserId") Long roomUserId);

    @Query("select cp from ChattingParticipate cp where cp.roomUser.id = :roomUserId")
    List<ChattingParticipate> findAllByRoomUserId(@Param("roomUserId") Long roomUserId);

    @Query("select cp from ChattingParticipate cp join fetch cp.chattingRoom cr where cp.chattingRoom.id in :chattingRoomIdList")
    List<ChattingParticipate> findAllByChattingRoomIdsWithChattingRoom(@Param("chattingRoomIdList") List<Long> chattingRoomIdList);

    @Query("select cp from ChattingParticipate cp join fetch cp.chattingRoom cr join fetch cp.roomUser ru " +
            "where cp.chattingRoom.id = :chattingRoomId and cp.roomUser.id != :roomUserId")
    List<ChattingParticipate> findAllByChattingRoomIdAndRoomUserIdNotWithChattingRoomAndRoomUser
            (@Param("chattingRoomId") Long chattingRoomId, @Param("roomUserId") Long roomUserId);
}
