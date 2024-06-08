package com.harrishjoshi.springaop.audit.trails.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TokenRepository extends JpaRepository<Token, Integer> {

    @Query("SELECT t FROM Token t WHERE t.user.id = :id AND t.revoked = false AND t.expired = false")
    List<Token> findAllValidTokenByUser(Integer id);
}
