package com.emelmujiro.secreto.feed.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.emelmujiro.secreto.feed.entity.Feed;

public interface FeedRepository extends JpaRepository<Feed, Long> {

	@Query("select f from Feed f where f.id = :id and f.author.id = :authorId and f.deletedYn = false ")
	Optional<Feed> findByIdAndAuthorId(@Param("id") Long id, @Param("authorId") Long authorId);

	@Query("select f from Feed f where f.id = :id and f.deletedYn = false ")
	Optional<Feed> findActiveById(@Param("id") Long id);
}
