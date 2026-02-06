package dev.fjcodes.repository;

import dev.fjcodes.model.VehicleType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VehicleTypeRepository extends JpaRepository<VehicleType, Long> {
	Optional<VehicleType> findByNameIgnoreCase(String name);
}
