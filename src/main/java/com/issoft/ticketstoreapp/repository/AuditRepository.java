package com.issoft.ticketstoreapp.repository;

import com.issoft.ticketstoreapp.model.audit.Audit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditRepository extends JpaRepository<Audit, Long> {
}
