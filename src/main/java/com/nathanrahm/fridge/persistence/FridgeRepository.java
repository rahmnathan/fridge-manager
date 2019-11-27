package com.nathanrahm.fridge.persistence;

import com.nathanrahm.fridge.persistence.entity.Fridge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FridgeRepository extends JpaRepository<Fridge, Long> {
}
