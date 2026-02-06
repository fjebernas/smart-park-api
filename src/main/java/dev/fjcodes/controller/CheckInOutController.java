package dev.fjcodes.controller;

import dev.fjcodes.service.CheckInOutService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("${serverBaseUrl}")
public class CheckInOutController {
	private final CheckInOutService checkInOutService;

	/**
	 * Checks a vehicle into a parking lot.
	 *
	 * @param lotId the ID of the parking lot
	 * @param plate the license plate of the vehicle
	 * @return HTTP 200 if the vehicle is successfully checked in
	 */
	@PostMapping("/parkingLots/{lotId}/checkin/{plate}")
	public ResponseEntity<?> checkInVehicle(@PathVariable String lotId, @PathVariable String plate) {
		checkInOutService.checkInVehicle(lotId, plate);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	/**
	 * Checks a vehicle out of a parking lot.
	 *
	 * @param lotId the ID of the parking lot
	 * @param plate the license plate of the vehicle
	 * @return HTTP 200 if the vehicle is successfully checked out
	 */
	@PostMapping("/parkingLots/{lotId}/checkout/{plate}")
	public ResponseEntity<?> checkOutVehicle(@PathVariable String lotId, @PathVariable String plate) {
		checkInOutService.checkOutVehicle(lotId, plate);
		return new ResponseEntity<>(HttpStatus.OK);
	}
}
