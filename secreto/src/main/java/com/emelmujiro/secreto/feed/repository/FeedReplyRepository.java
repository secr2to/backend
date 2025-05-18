package com.emelmujiro.secreto.feed.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.emelmujiro.secreto.feed.entity.FeedReply;

public interface FeedReplyRepository extends JpaRepository<FeedReply, Long> {

	@Query("select r from FeedReply r where r.id = :id and r.deletedYn = false ")
	Optional<FeedReply> findActiveById(@Param("id") Long id);

	@Query("select r from FeedReply r where r.id = :id and r.replier.id = :replierId and r.deletedYn = false ")
	Optional<FeedReply> findByIdAndReplierId(@Param("id") Long id, @Param("replierId") Long replierId);
}
