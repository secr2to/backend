package com.emelmujiro.secreto.room.repository;

import com.emelmujiro.secreto.room.entity.RoomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RoomUserRepository extends JpaRepository<RoomUser, Long> {

    @Query("select ru from RoomUser ru where ru.user.id = :userId and ru.room.id = :roomId")
    Optional<RoomUser> findByUserIdAndRoomId(@Param("userId") Long userId, @Param("roomId") Long roomId);

    @Query("select ru from RoomUser ru where ru.user.id = :userId")
    List<RoomUser> findAllByUserId(@Param("userId") long userId);
}
