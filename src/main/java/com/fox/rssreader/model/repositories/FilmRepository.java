package com.fox.rssreader.model.repositories;

import com.fox.rssreader.model.entities.Film;
import com.fox.rssreader.model.entities.ExternalFilmInfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FilmRepository extends JpaRepository<Film, Long> {
    List<Film> findAllByIgnore(Boolean ignore);
}
