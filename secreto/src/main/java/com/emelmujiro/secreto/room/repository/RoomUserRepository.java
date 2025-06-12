package com.emelmujiro.secreto.room.repository;

import com.emelmujiro.secreto.room.entity.RoomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface RoomUserRepository extends JpaRepository<RoomUser, Long> {

    @Query("select ru from RoomUser ru where ru.user.id = :userId and ru.room.id = :roomId")
    Optional<RoomUser> findByUserIdAndRoomId(@Param("userId") long userId, @Param("roomId") long roomId);

    @Query("select ru from RoomUser ru where ru.user.id = :userId")
    List<RoomUser> findAllByUserId(@Param("userId") long userId);

    @Query("select ru from RoomUser ru left join fetch ru.roomCharacter rc left join fetch ru.roomProfile rp where ru.room.id = :roomId")
    List<RoomUser> findAllByRoomIdWithRoomCharacterAndRoomProfile(@Param("roomId") long roomId);

    @Query("select ru from RoomUser ru where ru.room.id = :roomId and ru.standbyYn = :standbyYn")
    List<RoomUser> findAllByRoomIdAndStandbyYn(@Param("roomId") long roomId, @Param("standbyYn") Boolean standbyYn);

    @Query("select ru from RoomUser ru left join fetch ru.roomCharacter rc left join fetch ru.roomProfile rp join fetch ru.user u where ru.id = :roomUserId and ru.room.id = :roomId")
    Optional<RoomUser> findByIdAndRoomIdWithRoomCharacterAndRoomProfileAndUser(@Param("roomUserId") long roomUserId, @Param("roomId") long roomId);

    @Query("select ru from RoomUser ru left join fetch ru.roomCharacter rc left join fetch ru.roomProfile join fetch ru.user u where ru.room.id = :roomId")
    List<RoomUser> findAllByRoomIdWithRoomCharacterAndRoomProfileAndUser(@Param("roomId") long roomId);

    @Query("select count(ru) from RoomUser ru where ru.room.id = :roomId and ru.standbyYn = :standbyYn")
    int countByRoomIdAndStandbyYn(@Param(("roomId")) Long roomId, @Param("standbyYn") Boolean standbyYn);

    @Query("select ru from RoomUser ru where ru.room.id = :roomId and ru.managerYn = :managerYn")
    RoomUser findByRoomIdAndManagerYn(@Param("roomId") Long roomId, @Param("managerYn") Boolean managerYn);
}
