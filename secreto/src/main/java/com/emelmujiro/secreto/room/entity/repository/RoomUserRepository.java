package com.emelmujiro.secreto.room.entity.repository;

import com.emelmujiro.secreto.room.entity.RoomUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomUserRepository extends JpaRepository<RoomUser, Long> {
}
