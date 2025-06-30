package com.emelmujiro.secreto.room.repository;

import com.emelmujiro.secreto.mission.entity.RoomMission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface RoomMissionRepository extends JpaRepository<RoomMission, Long> {

    @Query("select rm from RoomMission rm where rm.room.id = :roomId and rm.executeYn = :executeYn")
    List<RoomMission> findAllByRoomIdAndExecuteYn(@Param("roomId") Long roomId, @Param("executeYn") Boolean executeYn);

    @Query("select rm from RoomMission rm where rm.room.id = :roomId")
    List<RoomMission> findAllByRoomId(@Param("roomId") Long roomId);
}
