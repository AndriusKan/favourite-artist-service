package com.favouriteartistservice;

import com.favouriteartistservice.domain.Album;
import com.favouriteartistservice.domain.Artist;
import com.favouriteartistservice.exception.ItunesIntegrationException;
import com.favouriteartistservice.itunes.ItunesService;
import com.favouriteartistservice.itunes.response.ItunesArtistSearchResponse;
import com.favouriteartistservice.repository.AlbumRepository;
import com.favouriteartistservice.repository.ArtistRepository;
import com.favouriteartistservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;

@RestController
@RequestMapping("/favourite-artist-service")
@RequiredArgsConstructor
public class FavouriteArtistController {

    private final ItunesService itunesService;

    private final FavouriteArtistService favouriteArtistService;

    @GetMapping(value = "/search/{artist}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ItunesArtistSearchResponse> searchArtist(@RequestHeader(value = "userId", defaultValue = "00001") String userId, @PathVariable String artist) throws ItunesIntegrationException {
        return ResponseEntity.ok(itunesService.searchArtists(artist));
    }

    @PostMapping(value = "/save-favourite-artist", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Artist> saveFavouriteArtist(@RequestHeader(value = "userId", defaultValue = "00001") String userId, @RequestBody Artist artist) {
        return ResponseEntity.ok(favouriteArtistService.saveFavouriteArtist(userId, artist));
    }

    @PostMapping(value = "/get-artist-albums", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Set<Album>> getArtistAlbums(@RequestHeader(value = "userId", defaultValue = "00001") String userId, @RequestBody Artist artist) throws ItunesIntegrationException {
        return ResponseEntity.ok(favouriteArtistService.getArtistAlbums(userId, artist));
    }

}
