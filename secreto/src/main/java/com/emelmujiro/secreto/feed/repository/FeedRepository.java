package com.emelmujiro.secreto.feed.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emelmujiro.secreto.feed.entity.Feed;

public interface FeedRepository extends JpaRepository<Feed, Long> {
}
