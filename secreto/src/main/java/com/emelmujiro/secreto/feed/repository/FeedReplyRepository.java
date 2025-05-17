package com.emelmujiro.secreto.feed.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emelmujiro.secreto.feed.entity.FeedReply;

public interface FeedReplyRepository extends JpaRepository<FeedReply, Long> {
}
