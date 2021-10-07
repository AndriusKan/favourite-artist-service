package com.favouriteartistservice.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Album {

    @Id
    private String collectionId;
    private String wrapperType;
    private String trackCount;
    private String releaseDate;
    private String primaryGenreName;
    private String currency;
    private String country;
    private String copyright;
    private String collectionViewUrl;
    private String collectionType;
    private String collectionPrice;
    private String collectionName;
    private String collectionExplicitness;
    private String collectionCensoredName;
    private String artworkUrl60;
    private String artworkUrl100;
    private String artistViewUrl;
    private String artistName;
    private String artistId;
    private String amgArtistId;
    private String contentAdvisoryRating;

    @ManyToOne
    @JoinColumn(name = "artist", nullable = false)
    @JsonIgnore
    private Artist artist;
}