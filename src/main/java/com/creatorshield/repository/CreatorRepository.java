package com.creatorshield.repository;

import com.creatorshield.domain.Creator;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CreatorRepository extends JpaRepository<Creator, Long> {

    Optional<Creator> findByExternalId(String externalId);
}
