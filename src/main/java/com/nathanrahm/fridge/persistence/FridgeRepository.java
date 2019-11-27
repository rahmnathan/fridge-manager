package com.nathanrahm.fridge.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FridgeRepository extends JpaRepository<Fridge, Long> {
    Optional<Fridge> findByFridgeId(String id);
    Optional<Fridge> findByName(String name);
}
