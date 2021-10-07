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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FavouriteArtistServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ArtistRepository artistRepository;

    @Mock
    private ItunesService itunesService;

    @Mock
    private AlbumRepository albumRepository;

    @InjectMocks
    private FavouriteArtistService favouriteArtistService;

    @Test
    public void testSaveFavouriteArtistForNewUser() {
        Artist artist = createNewArtist();
        when(userRepository.findById("userId")).thenReturn(Optional.empty());
        when(artistRepository.findById("amgArtistID")).thenReturn(Optional.empty());

        Artist result = favouriteArtistService.saveFavouriteArtist("userId", artist);

        assertEquals(result, artist);
    }

    @Test
    public void testSaveFavouriteArtistForExistingUserAndArtist() {
        Artist artist = createNewArtist();
        User user = createNewUser();
        when(userRepository.findById("userId")).thenReturn(Optional.of(user));
        when(artistRepository.findById("amgArtistID")).thenReturn(Optional.of(artist));

        Artist result = favouriteArtistService.saveFavouriteArtist("userId", artist);

        assertTrue(result.getUsers().contains(user));
    }

    @Test
    public void testGetArtistAlbums() throws ItunesIntegrationException {
        Artist artist = createNewArtist();
        User user = createNewUser();
        artist.getUsers().add(user);
        when(albumRepository.findByArtist_AmgArtistId("amgArtistID")).thenReturn(Collections.emptySet());
        when(userRepository.findById("userId")).thenReturn(Optional.of(user));
        when(itunesService.searchArtistAlbums(Collections.singletonList(artist))).thenReturn(createItunesAlbumSearchResponse());

        Set<Album> result = favouriteArtistService.getArtistAlbums("userId", artist);

        assertEquals(result.size(), 1);
    }

    @Test
    public void testRequestedArtistIsNotUsersFavourite() throws ItunesIntegrationException {
        Artist artist = createNewArtist();
        User user = createNewUser();
        when(userRepository.findById("userId")).thenReturn(Optional.of(user));

        Assertions.assertThrows(BadInputException.class, () -> {
            favouriteArtistService.getArtistAlbums("userId", artist);
        });
    }

    private Artist createNewArtist() {
        Artist result = new Artist();
        result.setAmgArtistId("amgArtistID");
        return result;
    }

    private User createNewUser() {
        User result = new User();
        result.setUserId("userId");
        return result;
    }

    private ItunesAlbumSearchResponse createItunesAlbumSearchResponse() {
        ItunesAlbumSearchResponse result = new ItunesAlbumSearchResponse();
        result.setResultCount(1);
        Album album = new Album();
        result.setResults(Collections.singleton(album));
        return result;
    }
}
