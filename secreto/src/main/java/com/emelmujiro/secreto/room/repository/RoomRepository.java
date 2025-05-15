package com.emelmujiro.secreto.room.repository;

import com.emelmujiro.secreto.room.entity.Room;
import com.emelmujiro.secreto.room.entity.RoomStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface RoomRepository extends JpaRepository<Room, Long> {

    @Query("select r from Room r where r.id in :roomIdList and r.roomStatus = :status")
    List<Room> findAllByIdsAndRoomStatus(@Param("roomIdList") List<Long> roomIdList, @Param("status") RoomStatus status);

    @Query("select r from Room r where r.code = :code")
    Optional<Room> findByCode(@Param("code") String code);
}
