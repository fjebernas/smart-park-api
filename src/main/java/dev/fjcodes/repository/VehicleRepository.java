
package dev.fjcodes.repository;

import dev.fjcodes.model.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle, String> {
    Optional<Vehicle> findByLicensePlate(String licensePlate);

    List<Vehicle> findByParkingLot_LotId(String lotId);
}
