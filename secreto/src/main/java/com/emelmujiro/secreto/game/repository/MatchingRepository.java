package com.emelmujiro.secreto.game.repository;

import com.emelmujiro.secreto.game.entity.Matching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MatchingRepository extends JpaRepository<Matching, Long> {

    @Query("select m from Matching m where m.roomUser.id = :roomUserId")
    Matching findByRoomUserId(@Param("roomUserId") Long roomUserId);
}
