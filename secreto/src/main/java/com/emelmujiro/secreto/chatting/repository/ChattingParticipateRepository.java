package com.emelmujiro.secreto.chatting.repository;

import com.emelmujiro.secreto.chatting.entity.ChattingParticipate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChattingParticipateRepository extends JpaRepository<ChattingParticipate, Long> {
}
