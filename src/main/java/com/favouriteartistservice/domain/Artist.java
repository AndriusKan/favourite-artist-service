package com.favouriteartistservice.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
@Getter
@Setter
public class Artist {

    @Id
    private String amgArtistId;
    private String artistId;
    private String wrapperType;
    private String artistType;
    private String artistName;
    private String artistLinkUrl;
    private String primaryGenreName;
    private String primaryGenreId;

    @ManyToMany(mappedBy = "artists")
    @JsonIgnore
    Set<User> users = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy="artist", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    Set<Album> albums = new HashSet<>();
}
