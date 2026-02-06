package dev.fjcodes.service.impl;

import dev.fjcodes.exception.ResourceAlreadyExistsException;
import dev.fjcodes.exception.ResourceNotFoundException;
import dev.fjcodes.model.Vehicle;
import dev.fjcodes.model.VehicleType;
import dev.fjcodes.repository.VehicleRepository;
import dev.fjcodes.repository.VehicleTypeRepository;
import dev.fjcodes.request.VehicleRequest;
import dev.fjcodes.response.VehicleResponse;
import dev.fjcodes.service.VehicleService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class VehicleServiceImpl implements VehicleService {
	private final VehicleRepository vehicleRepository;

	private final VehicleTypeRepository vehicleTypeRepository;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public VehicleResponse registerVehicle(VehicleRequest vehicleRequest) {
		Vehicle vehicle = new Vehicle();
		vehicle.setLicensePlate(vehicleRequest.getLicensePlate())
				.setOwnerName(vehicleRequest.getOwnerName());

		VehicleType vehicleType = vehicleTypeRepository
				.findByNameIgnoreCase(vehicleRequest.getType())
				.orElseThrow(() -> new ResourceNotFoundException("Vehicles of type " + vehicleRequest.getType() + " not supported."));

		vehicle.setVehicleType(vehicleType);

		try {
			Vehicle savedVehicle = vehicleRepository.save(vehicle);

			return new VehicleResponse()
					.setLicensePlate(savedVehicle.getLicensePlate())
					.setOwnerName(savedVehicle.getOwnerName())
					.setType(savedVehicle.getVehicleType().getName());
		} catch (DataIntegrityViolationException e) {
			throw new ResourceAlreadyExistsException("Vehicle with plate: " + vehicleRequest.getLicensePlate() + " already exists.");
		}
	}
}
