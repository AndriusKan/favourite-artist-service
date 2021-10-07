package com.favouriteartistservice;

import com.favouriteartistservice.domain.Album;
import com.favouriteartistservice.domain.Artist;
import com.favouriteartistservice.domain.User;
import com.favouriteartistservice.exception.ItunesIntegrationException;
import com.favouriteartistservice.itunes.ItunesService;
import com.favouriteartistservice.itunes.response.ItunesAlbumSearchResponse;
import com.favouriteartistservice.repository.ArtistRepository;
import com.favouriteartistservice.scheduler.AlbumSyncScheduler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AlbumSyncSchedulerTest {

    @Mock
    private ItunesService itunesService;

    @Mock
    private ArtistRepository artistRepository;

    @InjectMocks
    private AlbumSyncScheduler albumSyncScheduler;

    @Test
    void testAlbumSync() throws ItunesIntegrationException {
        Set<Album> itunesReturnedAlbums = new java.util.HashSet<>(Collections.emptySet());
        Album album = new Album();
        album.setAmgArtistId("amgArtistID");
        Album album2 = new Album();
        album2.setAmgArtistId("amgArtistID2");
        itunesReturnedAlbums.add(album);
        itunesReturnedAlbums.add(album2);
        Artist artist = createNewArtist();
        artist.getAlbums().add(album);
        List<Artist> allCachedArtists = Collections.singletonList(artist);
        ItunesAlbumSearchResponse itunesAlbumSearchResponse = new ItunesAlbumSearchResponse();
        itunesAlbumSearchResponse.setResults(itunesReturnedAlbums);
        when(artistRepository.findAll()).thenReturn(allCachedArtists);
        when(itunesService.searchArtistAlbums(allCachedArtists)).thenReturn(itunesAlbumSearchResponse);

        albumSyncScheduler.startAlbumSync();

        verify(artistRepository, times(1)).save(artist);
        assertEquals(artist.getAlbums().size(), 1);
    }

    private Artist createNewArtist() {
        Artist result = new Artist();
        result.setAmgArtistId("amgArtistID");
        return result;
    }
}
