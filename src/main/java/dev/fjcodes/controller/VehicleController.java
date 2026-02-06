package dev.fjcodes.controller;

import dev.fjcodes.request.VehicleRequest;
import dev.fjcodes.response.VehicleResponse;
import dev.fjcodes.service.VehicleService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@AllArgsConstructor
@RestController
@RequestMapping("${serverBaseUrl}")
public class VehicleController {
	private final VehicleService vehicleService;

	/**
	 * Registers a new vehicle.
	 *
	 * @param vehicleRequest the vehicle registration request payload
	 * @return HTTP 201 with the created vehicle details
	 */
	@PostMapping("/vehicles")
	public ResponseEntity<VehicleResponse> registerVehicle(@RequestBody @Valid VehicleRequest vehicleRequest) {
		VehicleResponse vehicleResponse = vehicleService.registerVehicle(vehicleRequest);
		return new ResponseEntity<>(vehicleResponse, HttpStatus.CREATED);
	}
}
