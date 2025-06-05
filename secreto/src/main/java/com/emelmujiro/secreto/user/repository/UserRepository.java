package com.emelmujiro.secreto.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.emelmujiro.secreto.user.entity.User;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

	@Query("SELECT u FROM User u WHERE u.oAuthProvider = :provider AND u.username = :username AND u.deletedYn = false ")
	Optional<User> findOAuthUserByUsername(@Param("provider") String provider, @Param("username") String username);

	@Query("SELECT u FROM User u WHERE u.searchId = :searchId AND u.deletedYn = false ")
	Optional<User> findBySearchId(@Param("searchId") String searchId);

	@Query("SELECT u FROM User u WHERE u.id = :id AND u.deletedYn = false ")
	Optional<User> findActiveById(@Param("id") Long id);
}
