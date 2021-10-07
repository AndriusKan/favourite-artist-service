package com.favouriteartistservice.itunes.response;

import com.favouriteartistservice.domain.Artist;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class ItunesArtistSearchResponse {

    private int resultCount;
    private Set<Artist> results;
}
