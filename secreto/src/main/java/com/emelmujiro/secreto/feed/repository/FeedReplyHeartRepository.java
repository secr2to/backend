package com.emelmujiro.secreto.feed.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.emelmujiro.secreto.feed.entity.FeedReplyHeart;

public interface FeedReplyHeartRepository extends JpaRepository<FeedReplyHeart, Long> {

	@Query("select h from FeedReplyHeart h where h.feedReply.id = :replyId and h.user.id = :userId")
	Optional<FeedReplyHeart> findByReplyIdAndUserId(@Param("replyId") Long replyId, @Param("userId") Long userId);
}
