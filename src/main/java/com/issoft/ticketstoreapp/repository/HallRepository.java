package com.issoft.ticketstoreapp.repository;

import com.issoft.ticketstoreapp.model.Hall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface HallRepository extends JpaRepository<Hall, Long> {
    Optional<Hall> findByName(String name);

    @Query(value = "SELECT h FROM Hall h INNER JOIN h.cinema c WHERE c.id = :cinemaId")
    List<Hall> findHallsByCinemaId(Long cinemaId);
}