package com.fox.rssreader.controllers.imdb;

import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.reactive.function.client.WebClient;

@Controller
@AllArgsConstructor
public class ImdbProxyController {

    @PostMapping("/imdb")
    public ResponseEntity<ByteArrayResource> imdbApi(@RequestBody String request) {
        WebClient webClient = WebClient.create();

        var data = webClient.post()
                .uri("https://api.graphql.imdb.com/")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve().toEntity(ByteArrayResource.class);

//        ResponseEntity<ByteArrayResource> response = new ResponseEntity<>();
//        response.getHeaders().add(imdbResponse.);
        return data.block();
    }
}
