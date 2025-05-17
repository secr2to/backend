package com.emelmujiro.secreto.feed.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.emelmujiro.secreto.feed.entity.FeedHeart;

public interface FeedHeartRepository extends JpaRepository<FeedHeart, Long> {

	@Query("select fh from FeedHeart fh where fh.feed.id = :feedId and fh.user.id = :userId")
	Optional<FeedHeart> findByFeedIdAndUserId(@Param("feedId") Long feedId, @Param("userId") Long userId);
}
