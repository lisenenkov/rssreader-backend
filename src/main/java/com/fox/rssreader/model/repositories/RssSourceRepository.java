package com.fox.rssreader.model.repositories;

import com.fox.rssreader.model.entities.RssSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RssSourceRepository extends JpaRepository<RssSource, Long> {
    List<RssSource> findByDisabledIsFalse();
}