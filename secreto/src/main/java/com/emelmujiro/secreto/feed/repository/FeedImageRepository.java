package com.emelmujiro.secreto.feed.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.emelmujiro.secreto.feed.entity.FeedImage;

public interface FeedImageRepository extends JpaRepository<FeedImage, Long> {

	@Query("select h from FeedImage h where h.feed.id in :feedIds")
	List<FeedImage> findAllByFeedIdIn(@Param("feedIds") List<Long> feedIds);
}
