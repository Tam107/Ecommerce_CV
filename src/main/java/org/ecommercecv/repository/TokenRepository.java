package org.ecommercecv.repository;

import org.ecommercecv.model.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query(value = "SELECT t from Token t inner join t.user u "+
            "WHERE u.id = :userId and (t.expired = false  or t.revoked = false ) ")
    List<Token> findAllValidTokensByUser(Long userId);

    Optional<Token> findByAccessToken(String token);

    Optional<Token> findByRefreshToken(String refreshToken);
}
