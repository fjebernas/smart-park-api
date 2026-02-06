package dev.fjcodes.service;

import dev.fjcodes.exception.ResourceAlreadyExistsException;
import dev.fjcodes.exception.ResourceNotFoundException;
import dev.fjcodes.request.ParkingLotRequest;
import dev.fjcodes.response.ParkingLotResponse;
import dev.fjcodes.response.VehicleResponse;

import java.util.List;

public interface ParkingLotService {
	/**
	 * Registers a new parking lot.
	 *
	 * @param parkingLotRequest the parking lot registration request
	 * @return the registered parking lot details
	 * @throws IllegalArgumentException if occupied spaces exceed capacity
	 * @throws ResourceAlreadyExistsException if a parking lot with the same ID already exists
	 */
	ParkingLotResponse registerParkingLot(ParkingLotRequest parkingLotRequest);

	/**
	 * Retrieves the current status of a parking lot.
	 *
	 * @param lotId the ID of the parking lot
	 * @return the parking lot status
	 * @throws ResourceNotFoundException if the parking lot does not exist
	 */
	ParkingLotResponse getStatusOfParkingLot(String lotId);

	/**
	 * Retrieves all vehicles currently parked in a parking lot.
	 *
	 * @param lotId the ID of the parking lot
	 * @return the list of vehicles in the parking lot
	 * @throws ResourceNotFoundException if the parking lot does not exist
	 */
	List<VehicleResponse> getVehiclesFromParkingLot(String lotId);
}
