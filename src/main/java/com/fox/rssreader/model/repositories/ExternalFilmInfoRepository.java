package com.fox.rssreader.model.repositories;

import com.fox.rssreader.model.entities.ExternalFilmInfo;
import com.fox.rssreader.model.ids.ExternalFilmInfoId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExternalFilmInfoRepository extends JpaRepository<ExternalFilmInfo, ExternalFilmInfoId> {
}
