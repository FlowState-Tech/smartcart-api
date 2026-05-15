package com.smartcart.experience.infrastructure.persistence.jpa.repositories;

import com.smartcart.experience.domain.model.entities.StoreExperience;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.List;

@Repository
public interface StoreExperienceRepository extends JpaRepository<StoreExperience, String> {
    Optional<StoreExperience> findByRecorridoId(String recorridoId);
    List<StoreExperience> findByStoreId(String storeId);
    List<StoreExperience> findByBuyerId(String buyerId);
}