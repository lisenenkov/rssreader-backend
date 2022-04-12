package com.fox.rssreader.model.repositories;

import com.fox.rssreader.model.entities.FilmLink;

import com.fox.rssreader.model.ids.FilmLinkId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FilmLinkRepository extends JpaRepository<FilmLink, FilmLinkId> {

}
