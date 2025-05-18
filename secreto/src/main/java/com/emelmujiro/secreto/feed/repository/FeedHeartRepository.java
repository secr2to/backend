package com.emelmujiro.secreto.feed.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.emelmujiro.secreto.feed.entity.FeedHeart;
import com.emelmujiro.secreto.user.entity.User;

public interface FeedHeartRepository extends JpaRepository<FeedHeart, Long> {

	@Query("select h from FeedHeart h where h.feed.id = :feedId and h.user.id = :userId")
	Optional<FeedHeart> findByFeedIdAndUserId(@Param("feedId") Long feedId, @Param("userId") Long userId);

	@Query("select h from FeedHeart h join fetch h.user where h.feed.id = :feedId ")
	List<FeedHeart> findByFeedIdWithUser(@Param("feedId") Long feedId);

	@Query("select h from FeedHeart h join fetch h.user where h.feed.id in :feedIds")
	List<FeedHeart> findAllByFeedIdInWithUser(@Param("feedIds") List<Long> feedIds);
}
