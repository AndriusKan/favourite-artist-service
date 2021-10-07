package com.favouriteartistservice;

import com.favouriteartistservice.domain.Album;
import com.favouriteartistservice.domain.Artist;
import com.favouriteartistservice.domain.User;
import com.favouriteartistservice.exception.ItunesIntegrationException;
import com.favouriteartistservice.exception.BadInputException;
import com.favouriteartistservice.itunes.ItunesService;
import com.favouriteartistservice.itunes.response.ItunesAlbumSearchResponse;
import com.favouriteartistservice.repository.AlbumRepository;
import com.favouriteartistservice.repository.ArtistRepository;
import com.favouriteartistservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class FavouriteArtistService {

    private final ItunesService itunesService;

    private final UserRepository userRepository;

    private final AlbumRepository albumRepository;

    private final ArtistRepository artistRepository;

    public Artist saveFavouriteArtist(String userId, Artist artist) {
        if (artist.getAmgArtistId() == null) {
            throw new BadInputException("Artist without id cannot be processed");
        }
        User user = userRepository.findById(userId).orElse(new User());
        Artist artistToUse = artistRepository.findById(artist.getAmgArtistId()).orElse(artist);
        if (user.getUserId() == null) {
            user.setUserId(userId);
        }
        user.getArtists().add(artistToUse);
        artistToUse.getUsers().add(user);
        userRepository.save(user);
        return artistToUse;
    }

    public Set<Album> getArtistAlbums(String userId, Artist artist) throws ItunesIntegrationException {
        User user = userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new BadInputException("User is not yet registered");
        } else if (artist.getAmgArtistId() == null) {
            throw new BadInputException("Artist without id cannot be processed");
        } else if (!isArtistUsersFavourite(artist, userId)) {
            throw new BadInputException("Requested artist is not in users favourites list");
        }
        Set<Album> artistAlbums = albumRepository.findByArtist_AmgArtistId(artist.getAmgArtistId());
        if (!artistAlbums.isEmpty()) {
            return artistAlbums;
        } else {
            ItunesAlbumSearchResponse itunesAlbumSearchResponse = itunesService.searchArtistAlbums(Collections.singletonList(artist));
            itunesAlbumSearchResponse.getResults().forEach(album -> {
                album.setArtist(artist);
            });
            albumRepository.saveAll(itunesAlbumSearchResponse.getResults());
            return itunesAlbumSearchResponse.getResults();
        }
    }

    private boolean isArtistUsersFavourite(Artist artist, String userId) {
        return artist.getUsers().stream().anyMatch(user -> user.getUserId().equals(userId));
    }
}
