package dev.fjcodes.service;

public interface CheckInOutService {
	/**
	 * Checks a vehicle into a parking lot.
	 *
	 * @param lotId the ID of the parking lot
	 * @param plate the license plate of the vehicle
	 */
	void checkInVehicle(String lotId, String plate);

	/**
	 * Checks a vehicle out of a parking lot.
	 *
	 * @param lotId the ID of the parking lot
	 * @param plate the license plate of the vehicle
	 */
	void checkOutVehicle(String lotId, String plate);
}
