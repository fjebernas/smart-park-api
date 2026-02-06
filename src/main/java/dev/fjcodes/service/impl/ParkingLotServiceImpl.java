package dev.fjcodes.service.impl;

import dev.fjcodes.exception.ResourceAlreadyExistsException;
import dev.fjcodes.exception.ResourceNotFoundException;
import dev.fjcodes.model.ParkingLot;
import dev.fjcodes.model.Vehicle;
import dev.fjcodes.repository.ParkingLotRepository;
import dev.fjcodes.repository.VehicleRepository;
import dev.fjcodes.request.ParkingLotRequest;
import dev.fjcodes.response.ParkingLotResponse;
import dev.fjcodes.response.VehicleResponse;
import dev.fjcodes.service.ParkingLotService;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@Service
public class ParkingLotServiceImpl implements ParkingLotService {
	private final ParkingLotRepository parkingLotRepository;

	private final VehicleRepository vehicleRepository;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ParkingLotResponse registerParkingLot(ParkingLotRequest parkingLotRequest) {
		if (parkingLotRequest.getCapacity() < parkingLotRequest.getOccupiedSpaces()) {
			throw new IllegalArgumentException("Occupied Spaces cannot be more than the capacity");
		}

		ParkingLot parkingLot = new ParkingLot();
		BeanUtils.copyProperties(parkingLotRequest, parkingLot);

		try {
			ParkingLot savedParkingLot = parkingLotRepository.save(parkingLot);

			return new ParkingLotResponse()
					.setLotId(savedParkingLot.getLotId())
					.setLocation(savedParkingLot.getLocation())
					.setCapacity(savedParkingLot.getCapacity())
					.setOccupiedSpaces(savedParkingLot.getOccupiedSpaces());
		} catch (DataIntegrityViolationException e) {
			throw new ResourceAlreadyExistsException("Parking Lot with Lot ID: " + parkingLotRequest.getLotId() + " already exists.");
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ParkingLotResponse getStatusOfParkingLot(String lotId) {
		ParkingLot parkingLot = parkingLotRepository
				.findByLotId(lotId)
				.orElseThrow(() -> new ResourceNotFoundException("Parking Lot with Lot ID: " + lotId + " not found."));
		return new ParkingLotResponse()
				.setLotId(parkingLot.getLotId())
				.setLocation(parkingLot.getLocation())
				.setCapacity(parkingLot.getCapacity())
				.setOccupiedSpaces(parkingLot.getOccupiedSpaces());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<VehicleResponse> getVehiclesFromParkingLot(String lotId) {
		if (!parkingLotRepository.findByLotId(lotId).isPresent()) throw new ResourceNotFoundException("Parking Lot with Lot ID: " + lotId + " not found.");
		List<Vehicle> vehicles = vehicleRepository.findByParkingLot_LotId(lotId);
		return vehicles
				.stream()
				.map(v -> new VehicleResponse()
						.setLicensePlate(v.getLicensePlate())
						.setOwnerName(v.getOwnerName())
						.setType(v.getVehicleType().getName()))
				.collect(Collectors.toList());
	}
}
