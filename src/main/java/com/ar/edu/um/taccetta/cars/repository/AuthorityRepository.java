package com.ar.edu.um.taccetta.cars.repository;

import com.ar.edu.um.taccetta.cars.domain.Authority;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for the {@link Authority} entity.
 */
public interface AuthorityRepository extends JpaRepository<Authority, String> {}
