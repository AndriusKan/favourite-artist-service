package com.favouriteartistservice;

import com.favouriteartistservice.domain.Artist;
import com.favouriteartistservice.exception.ItunesIntegrationException;
import com.favouriteartistservice.itunes.ItunesService;
import com.favouriteartistservice.itunes.response.ItunesAlbumSearchResponse;
import com.favouriteartistservice.itunes.response.ItunesArtistSearchResponse;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ItunesServiceTest {
    private static MockWebServer mockWebServer;

    private ItunesService itunesService;

    @BeforeEach
    public void setup() throws IOException {
        this.mockWebServer = new MockWebServer();
        this.mockWebServer.start();
        this.itunesService = new ItunesService(WebClient.create(), mockWebServer.url("/").toString());
    }

    @Test
    public void testItunesReturnsEmptyResponse() {
        MockResponse mockResponse = new MockResponse()
                .addHeader("Content-Type", "text/javascript; charset=utf-8")
                .setBody("");

        mockWebServer.enqueue(mockResponse);

        Assertions.assertThrows(ItunesIntegrationException.class, () -> {
            itunesService.searchArtists("Artist");
        });
    }

    @Test
    public void testArtistSearchByName() throws IOException, ItunesIntegrationException {
        File resource = new ClassPathResource("itunesSearchResponse.txt").getFile();
        String text = new String(Files.readAllBytes(resource.toPath()));
        MockResponse mockResponse = new MockResponse()
                .addHeader("Content-Type", "text/javascript; charset=utf-8")
                .setBody(text);

        mockWebServer.enqueue(mockResponse);

        ItunesArtistSearchResponse response = itunesService.searchArtists("Artist");

        assertAll("Should return correct ItunesArtistSearchResponse",
                () -> assertEquals(response.getResultCount(), 1),
                () -> assertEquals(response.getResults().size(), 1));
    }

    @Test
    public void testArtistAlbumsSearch() throws IOException, ItunesIntegrationException {
        File resource = new ClassPathResource("itunesAlbumSearchResponse.txt").getFile();
        String text = new String(Files.readAllBytes(resource.toPath()));
        MockResponse mockResponse = new MockResponse()
                .addHeader("Content-Type", "text/javascript; charset=utf-8")
                .setBody(text);

        mockWebServer.enqueue(mockResponse);

        ItunesAlbumSearchResponse response = itunesService.searchArtistAlbums(Collections.singletonList(new Artist()));

        assertAll("Should return correct ItunesAlbumSearchResponse",
                () -> assertEquals(response.getResultCount(), 6),
                () -> assertEquals(response.getResults().size(), 5));
    }
}
