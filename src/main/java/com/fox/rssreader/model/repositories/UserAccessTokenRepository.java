package com.fox.rssreader.model.repositories;

import com.fox.rssreader.model.entities.UserAccessToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAccessTokenRepository extends JpaRepository<UserAccessToken, Long> {
}