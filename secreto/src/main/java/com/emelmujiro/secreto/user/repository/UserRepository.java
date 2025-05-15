package com.emelmujiro.secreto.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.emelmujiro.secreto.user.entity.User;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {

	@Query("SELECT u FROM User u WHERE u.oAuthProvider = :provider AND u.username = :username")
	Optional<User> findOAuthUserByUsername(@Param("provider") String provider, @Param("username") String username);
}
