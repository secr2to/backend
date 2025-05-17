package com.emelmujiro.secreto.feed.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emelmujiro.secreto.feed.entity.FeedTagUser;

public interface FeedTagUserRepository extends JpaRepository<FeedTagUser, Long> {
}
