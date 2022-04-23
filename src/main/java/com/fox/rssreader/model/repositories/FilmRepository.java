package com.fox.rssreader.model.repositories;

import com.fox.rssreader.model.entities.Film;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilmRepository extends JpaRepository<Film, Long> {
    List<Film> findAllByIgnore(Boolean ignore, Sort sort);

    @Query("""
            SELECT DISTINCT f\s
            FROM Film f JOIN FETCH f.links fl
            WHERE :ignore IS NULL OR f.ignore = :ignore
            ORDER BY f.ignore ASC, fl.viewed ASC, fl.pubDate DESC""")
    List<Film> findLastPublished(Boolean ignore);
}
