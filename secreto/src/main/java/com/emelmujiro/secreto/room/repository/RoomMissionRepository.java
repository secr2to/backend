package com.emelmujiro.secreto.room.repository;

import com.emelmujiro.secreto.mission.entity.RoomMission;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomMissionRepository extends JpaRepository<RoomMission, Long> {
}
