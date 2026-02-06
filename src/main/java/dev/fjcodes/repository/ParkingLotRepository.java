
package dev.fjcodes.repository;

import dev.fjcodes.model.ParkingLot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ParkingLotRepository extends JpaRepository<ParkingLot, String> {
    Optional<ParkingLot> findByLotId(String lotId);
}
