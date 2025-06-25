package com.emerson.authservice.adapters.out.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
    Optional<RefreshTokenEntity> findByToken(String token);
    
    @Modifying
    @Query("DELETE FROM RefreshTokenEntity r WHERE r.expiresAt < ?1")
    void deleteExpiredTokens(Instant now);
    
    @Modifying
    @Query("DELETE FROM RefreshTokenEntity r WHERE r.user.id = ?1")
    void deleteByUserId(Long userId);
} 