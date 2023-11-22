package com.resumebuilder.auth;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.resumebuilder.user.User;


@Repository
public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Long>{

	Optional<PasswordResetToken> findByToken(String token);
	
	PasswordResetToken deleteByToken(PasswordResetToken token);

	@Query("SELECT t FROM PasswordResetToken t WHERE t.user = :user AND t.used = false")
    Optional<PasswordResetToken> findByUserAndUsedIsFalse(@Param("user") User user);
}
