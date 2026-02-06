package dev.fjcodes.service;

import dev.fjcodes.exception.ResourceAlreadyExistsException;
import dev.fjcodes.exception.ResourceNotFoundException;
import dev.fjcodes.request.VehicleRequest;
import dev.fjcodes.response.VehicleResponse;

public interface VehicleService {
	/**
	 * Registers a new vehicle in the system.
	 *
	 * @param vehicleRequest the vehicle registration request
	 * @return the registered vehicle details
	 * @throws ResourceNotFoundException if the vehicle type is not supported
	 * @throws ResourceAlreadyExistsException if a vehicle with the same license plate already exists
	 */
	VehicleResponse registerVehicle(VehicleRequest vehicleRequest);
}
