package com.issoft.ticketstoreapp.repository;

import com.issoft.ticketstoreapp.model.Row;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RowRepository extends JpaRepository<Row, Long> {
}
