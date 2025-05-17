package com.emelmujiro.secreto.feed.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.emelmujiro.secreto.feed.entity.FeedImage;

public interface FeedImageRepository extends JpaRepository<FeedImage, Long> {
}
