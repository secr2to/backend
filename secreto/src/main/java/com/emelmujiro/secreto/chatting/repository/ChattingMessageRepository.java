package com.emelmujiro.secreto.chatting.repository;

import com.emelmujiro.secreto.chatting.entity.ChattingMessage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChattingMessageRepository extends JpaRepository<ChattingMessage, Long> {
}
