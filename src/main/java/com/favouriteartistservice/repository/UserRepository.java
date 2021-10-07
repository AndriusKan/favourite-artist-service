package com.favouriteartistservice.repository;

import com.favouriteartistservice.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByArtists_amgArtistId(String amgArtistId);
}
