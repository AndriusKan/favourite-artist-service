package com.favouriteartistservice.scheduler;

import com.favouriteartistservice.domain.Album;
import com.favouriteartistservice.domain.Artist;
import com.favouriteartistservice.exception.ItunesIntegrationException;
import com.favouriteartistservice.itunes.ItunesService;
import com.favouriteartistservice.repository.ArtistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AlbumSyncScheduler {

    private final ItunesService itunesService;

    private final ArtistRepository artistRepository;

    @Scheduled(cron = "0 */5 * * * ?")
    public void startAlbumSync() throws ItunesIntegrationException {
        List<Artist> allCachedArtists = artistRepository.findAll();
        Set<Album> allAlbums = itunesService.searchArtistAlbums(allCachedArtists).getResults();
        for (Artist artist : allCachedArtists) {
            Set<Album> retrievedArtistAlbums = allAlbums.stream()
                    .filter(album -> album.getAmgArtistId().equals(artist.getAmgArtistId())).collect(Collectors.toSet());
            retrievedArtistAlbums.forEach(album -> album.setArtist(artist));
            artist.setAlbums(retrievedArtistAlbums);
            artistRepository.save(artist);
        }
    }
}
