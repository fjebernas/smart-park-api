package dev.fjcodes.service.impl;

import dev.fjcodes.exception.IllegalCheckInOutException;
import dev.fjcodes.exception.ResourceNotFoundException;
import dev.fjcodes.model.ParkingLot;
import dev.fjcodes.model.Vehicle;
import dev.fjcodes.repository.ParkingLotRepository;
import dev.fjcodes.repository.VehicleRepository;
import dev.fjcodes.service.CheckInOutService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CheckInOutServiceImpl implements CheckInOutService {
	private final ParkingLotRepository parkingLotRepository;

	private final VehicleRepository vehicleRepository;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void checkInVehicle(String lotId, String plate) {
		ParkingLot lot = parkingLotRepository
				.findByLotId(lotId)
				.orElseThrow(() -> new ResourceNotFoundException("Parking Lot with Lot ID: " + lotId + " not found."));
		Vehicle vehicle = vehicleRepository
				.findByLicensePlate(plate)
				.orElseThrow(() -> new ResourceNotFoundException("Vehicle with plate: " + plate + " not found."));

		if (vehicle.getParkingLot() != null)
			throw new IllegalCheckInOutException("Vehicle already parked");

		if (lot.getOccupiedSpaces() >= lot.getCapacity())
			throw new IllegalCheckInOutException("Parking lot full");

		lot.setOccupiedSpaces(lot.getOccupiedSpaces() + 1);
		vehicle.setParkingLot(lot);

		parkingLotRepository.save(lot);
		vehicleRepository.save(vehicle);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void checkOutVehicle(String lotId, String plate) {
		ParkingLot lot = parkingLotRepository
				.findByLotId(lotId)
				.orElseThrow(() -> new ResourceNotFoundException("Parking lot with Lot ID: " + lotId + " not found."));
		Vehicle vehicle = vehicleRepository
				.findByLicensePlate(plate)
				.orElseThrow(() -> new ResourceNotFoundException("Vehicle with plate: " + plate + " not found."));

		if (vehicle.getParkingLot() == null || !vehicle.getParkingLot().getLotId().equals(lotId))
			throw new IllegalCheckInOutException("Vehicle not in this Parking Lot");

		lot.setOccupiedSpaces(lot.getOccupiedSpaces() - 1);
		vehicle.setParkingLot(null);

		parkingLotRepository.save(lot);
		vehicleRepository.save(vehicle);
	}
}
