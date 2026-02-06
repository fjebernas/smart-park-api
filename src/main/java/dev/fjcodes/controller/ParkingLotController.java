package dev.fjcodes.controller;

import dev.fjcodes.request.ParkingLotRequest;
import dev.fjcodes.response.ParkingLotResponse;
import dev.fjcodes.response.VehicleResponse;
import dev.fjcodes.service.ParkingLotService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("${serverBaseUrl}")
public class ParkingLotController {
	private final ParkingLotService parkingLotService;

	/**
	 * Registers a new parking lot.
	 *
	 * @param parkingLotRequest the parking lot registration request payload
	 * @return HTTP 201 with the created parking lot details
	 */
	@PostMapping("/parkingLots")
	public ResponseEntity<ParkingLotResponse> registerParkingLot(@RequestBody @Valid ParkingLotRequest parkingLotRequest) {
		ParkingLotResponse parkingLotResponse = parkingLotService.registerParkingLot(parkingLotRequest);
		return new ResponseEntity<>(parkingLotResponse, HttpStatus.CREATED);
	}

	/**
	 * Retrieves the current status of a parking lot.
	 *
	 * @param lotId the ID of the parking lot
	 * @return HTTP 200 with the parking lot status
	 */
	@GetMapping("/parkingLots/{lotId}/status")
	public ResponseEntity<ParkingLotResponse> getStatusOfParkingLot(@PathVariable String lotId) {
		ParkingLotResponse parkingLotResponse = parkingLotService.getStatusOfParkingLot(lotId);
		return new ResponseEntity<>(parkingLotResponse, HttpStatus.OK);
	}

	/**
	 * Retrieves all vehicles currently parked in a parking lot.
	 *
	 * @param lotId the ID of the parking lot
	 * @return HTTP 200 with the list of parked vehicles
	 */
	@GetMapping("/parkingLots/{lotId}/vehicles")
	public ResponseEntity<List<VehicleResponse>> getVehiclesFromParkingLot(@PathVariable String lotId) {
		List<VehicleResponse> vehicleResponses = parkingLotService.getVehiclesFromParkingLot(lotId);
		return new ResponseEntity<>(vehicleResponses, HttpStatus.OK);
	}
}
