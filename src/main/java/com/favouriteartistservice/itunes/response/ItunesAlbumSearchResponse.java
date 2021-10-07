package com.favouriteartistservice.itunes.response;

import com.favouriteartistservice.domain.Album;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class ItunesAlbumSearchResponse {

    private int resultCount;
    private Set<Album> results;
}
