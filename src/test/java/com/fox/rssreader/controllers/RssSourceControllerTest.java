package com.fox.rssreader.controllers;

import com.fox.rssreader.graphql.dto.RssSourceContentDTO;
import com.fox.rssreader.graphql.dto.RssSourceDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.tester.AutoConfigureGraphQlTester;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.graphql.test.tester.GraphQlTester;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureGraphQlTester
class RssSourceControllerTest {
    @Autowired
    private GraphQlTester graphQlTester;

    @Test
    void rssSourceCRUD() {
        var rssSourceContent = new RssSourceContentDTO();
        rssSourceContent.setName("name");
        rssSourceContent.setUrl("http://name");

        //create
        var creationResult = graphQlTester.document(
                        """
                                 mutation createRssSource($name: String, $url: String) {
                                   createRssSource(rssSource: { name: $name, url: $url }) { id }
                                }""")
                .variable("name", rssSourceContent.getName())
                .variable("url", rssSourceContent.getUrl())
                .execute()
                .path("createRssSource")
                .entity(RssSourceDTO.class)
                .get();

        //select
        graphQlTester.document(
                        """
                                query rssSource($id: ID!) {
                                  rssSource(id: $id) {
                                  name, url }
                                }""")
                .variable("id", creationResult.getId())
                .execute()
                .path("rssSource")
                .entity(RssSourceDTO.class)
                .satisfies(source -> {
                    assertThat(source.getName(), is(rssSourceContent.getName()));
                    assertThat(source.getUrl(), is(rssSourceContent.getUrl()));
                });

        //update
        final var new_name = "new_name";
        graphQlTester.document(
                        """
                                mutation updateRssSource($id: ID!, $name: String) {
                                  updateRssSource(id: $id, rssSource: {
                                    name: $name
                                  }) {
                                  name }
                                }""")
                .variable("id", creationResult.getId())
                .variable("name", new_name)
                .execute()
                .path("updateRssSource")
                .entity(RssSourceDTO.class)
                .satisfies(source -> {
                    assertThat(source.getName(), is(new_name));
                });

        //delete
        graphQlTester.document(
                        """
                                mutation deleteRssSource($id: ID!) {
                                  deleteRssSource(id: $id) {
                                  name }
                                }""")
                .variable("id", creationResult.getId())
                .execute()
                .path("deleteRssSource")
                .entity(RssSourceDTO.class)
                .satisfies(source -> {
                    assertThat(source.getName(), is(new_name));
                });

        //select
        graphQlTester.document(
                        """
                                query {
                                  rssSources {
                                  id, name }
                                }""")
                .variable("id", creationResult.getId())
                .execute()
                .path("rssSources")
                .entityList(RssSourceDTO.class)
                .satisfies(sourceList -> {
                    for (var source : sourceList) {
                        assertThat(source.getId(), is(not(creationResult.getId())));
                    }
                });
    }
}