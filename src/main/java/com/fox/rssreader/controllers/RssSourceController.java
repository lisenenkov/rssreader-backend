package com.fox.rssreader.controllers;

import com.fox.rssreader.graphql.dto.RssSourceContentDTO;
import com.fox.rssreader.graphql.dto.RssSourceDTO;
import com.fox.rssreader.model.repositories.RssSourceRepository;
import lombok.AllArgsConstructor;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import javax.transaction.Transactional;
import java.util.List;

@Controller
@AllArgsConstructor
public class RssSourceController {
    private final RssSourceRepository rssSourceRepository;

    @QueryMapping
    public RssSourceDTO rssSource(@Argument Long id) {
        var rssSource = rssSourceRepository.findById(id);
        if (rssSource.isEmpty()) {
            return null;
        }

        return new RssSourceDTO(rssSource.get());
    }

    @QueryMapping
    public List<RssSourceDTO> rssSources() {
        return rssSourceRepository.findAll()
                .stream()
                .map(RssSourceDTO::new)
                .toList();
    }

    @MutationMapping
    @Transactional
    public RssSourceDTO createRssSource(@Argument(name = "rssSource") RssSourceContentDTO rssSourceContent) {
        if (rssSourceContent == null) return null;
        var rssSource = rssSourceContent.toRssSource();
        rssSourceRepository.save(rssSource);

        return new RssSourceDTO(rssSource);
    }

    @MutationMapping
    @Transactional
    public RssSourceDTO updateRssSource(@Argument Long id, @Argument(name = "rssSource") RssSourceContentDTO rssSourceContent) {
        if (rssSourceContent == null) return null;
        var rssSource = rssSourceRepository.findById(id).orElseGet(() -> null);
        if (rssSource == null) return null;
        rssSourceContent.modifyRssSource(rssSource);
        rssSourceRepository.save(rssSource);

        return new RssSourceDTO(rssSource);
    }

    @MutationMapping
    @Transactional
    public RssSourceDTO deleteRssSource(@Argument Long id) {
        var rssSource = rssSourceRepository.findById(id);
        if (rssSource.isEmpty()) {
            return null;
        }

        rssSourceRepository.delete(rssSource.get());
        return new RssSourceDTO(rssSource.get());
    }

}
