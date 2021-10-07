package com.favouriteartistservice.itunes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.favouriteartistservice.domain.Artist;
import com.favouriteartistservice.exception.ItunesIntegrationException;
import com.favouriteartistservice.itunes.response.ItunesAlbumSearchResponse;
import com.favouriteartistservice.itunes.response.ItunesArtistSearchResponse;
import lombok.RequiredArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ItunesService {

    private final WebClient webClient;

    private final String itunesBaseUri;

    public ItunesService(WebClient webClient, @Value("${itunes.base-uri}") String itunesBaseUri) {
        this.webClient = webClient;
        this.itunesBaseUri = itunesBaseUri;
    }

    public ItunesArtistSearchResponse searchArtists(String artist) throws ItunesIntegrationException {
        ItunesArtistSearchResponse itunesArtistSearchResponse;
        String responseJson = webClient
                .post()
                .uri(itunesBaseUri, uriBuilder -> uriBuilder
                        .path("/search")
                        .queryParam("entity", "allArtist")
                        .queryParam("term", artist)
                        .build())
                .accept(MediaType.ALL)
                .retrieve()
                .bodyToMono(String.class)
                .log()
                .block();

        if (responseJson != null && !responseJson.isBlank()) {
            ObjectMapper mapper = new ObjectMapper();
            try {
                itunesArtistSearchResponse = mapper.readValue(responseJson, ItunesArtistSearchResponse.class);
            } catch (JsonProcessingException e) {
                throw new ItunesIntegrationException("Error while processing itunes response");
            }
        } else {
            throw new ItunesIntegrationException("Itunes response was empty");
        }

        return itunesArtistSearchResponse;
    }

    public ItunesAlbumSearchResponse searchArtistAlbums(List<Artist> artists) throws ItunesIntegrationException {
        ItunesAlbumSearchResponse itunesAlbumSearchResponse;
        String responseJson = webClient
                .post()
                .uri(itunesBaseUri, uriBuilder -> uriBuilder
                        .path("/lookup")
                        .queryParam("amgArtistId", buildQueryParam(artists))
                        .queryParam("entity", "album")
                        .queryParam("limit", 5)
                        .build())
                .accept(MediaType.ALL)
                .retrieve()
                .bodyToMono(String.class)
                .log()
                .block();
        if (responseJson != null && !responseJson.isBlank()) {
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                JsonNode jsonNode = objectMapper.readTree(responseJson);
                Iterator<JsonNode> nodes = jsonNode.get("results").elements();
                while (nodes.hasNext()) {
                    if (nodes.next().has("artistType")) {
                        nodes.remove();
                    }
                }
                itunesAlbumSearchResponse = objectMapper.treeToValue(jsonNode, ItunesAlbumSearchResponse.class);
            } catch (JsonProcessingException e) {
                throw new ItunesIntegrationException("Error while processing itunes response");
            }
        } else {
            throw new ItunesIntegrationException("Itunes response was empty");
        }
        return itunesAlbumSearchResponse;
    }

    private String buildQueryParam(List<Artist> artists) {
        return artists.stream().map(Artist::getAmgArtistId).collect(Collectors.joining(","));
    }
}
