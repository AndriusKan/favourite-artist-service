package com.favouriteartistservice.repository;

import com.favouriteartistservice.domain.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Set;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {
    Set<Album> findByArtist_AmgArtistId(String amgArtistId);
}
