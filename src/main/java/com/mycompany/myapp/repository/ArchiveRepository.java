package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Archive;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Archive entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ArchiveRepository extends JpaRepository<Archive, Long> {}
