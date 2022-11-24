package com.issoft.ticketstoreapp.repository;

import com.issoft.ticketstoreapp.model.Hall;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HallRepository extends JpaRepository<Hall, Long> {
    Optional<Hall> findByName(String name);
}