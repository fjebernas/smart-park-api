package service.impl;

import dev.fjcodes.exception.ResourceAlreadyExistsException;
import dev.fjcodes.exception.ResourceNotFoundException;
import dev.fjcodes.model.Vehicle;
import dev.fjcodes.model.VehicleType;
import dev.fjcodes.repository.VehicleRepository;
import dev.fjcodes.repository.VehicleTypeRepository;
import dev.fjcodes.request.VehicleRequest;
import dev.fjcodes.response.VehicleResponse;
import dev.fjcodes.service.impl.VehicleServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class VehicleServiceImplTest {
	private VehicleRepository vehicleRepository;

	private VehicleTypeRepository vehicleTypeRepository;

	private VehicleServiceImpl service;

	@BeforeEach
	void setup() {
		vehicleRepository = mock(VehicleRepository.class);
		vehicleTypeRepository = mock(VehicleTypeRepository.class);
		service = new VehicleServiceImpl(vehicleRepository, vehicleTypeRepository);
	}

	// ---------------- registerVehicle ----------------

	@Test
	void registerVehicle_success() {
		VehicleRequest vehicleRequest = new VehicleRequest();
		vehicleRequest.setLicensePlate("ABC-123");
		vehicleRequest.setOwnerName("John");
		vehicleRequest.setType("CAR");

		VehicleType vehicleType = new VehicleType();
		vehicleType.setName("CAR");

		when(vehicleTypeRepository.findByNameIgnoreCase("CAR"))
				.thenReturn(Optional.of(vehicleType));

		when(vehicleRepository.save(any(Vehicle.class)))
				.thenAnswer(inv -> inv.getArgument(0));

		VehicleResponse vehicleResponse = service.registerVehicle(vehicleRequest);

		assertEquals("ABC-123", vehicleResponse.getLicensePlate());
		assertEquals("John", vehicleResponse.getOwnerName());
		assertEquals("CAR", vehicleResponse.getType());

		verify(vehicleTypeRepository).findByNameIgnoreCase("CAR");
		verify(vehicleRepository).save(any(Vehicle.class));
	}

	@Test
	void registerVehicle_typeNotSupported() {
		VehicleRequest vehicleRequest = new VehicleRequest();
		vehicleRequest.setLicensePlate("ABC-123");
		vehicleRequest.setOwnerName("John");
		vehicleRequest.setType("UFO");

		when(vehicleTypeRepository.findByNameIgnoreCase("UFO"))
				.thenReturn(Optional.empty());

		ResourceNotFoundException ex = assertThrows(
				ResourceNotFoundException.class,
				() -> service.registerVehicle(vehicleRequest)
		);

		assertEquals("Vehicles of type UFO not supported.", ex.getMessage());
		verify(vehicleRepository, never()).save(any());
	}

	@Test
	void registerVehicle_duplicatePlate() {
		VehicleRequest vehicleRequest = new VehicleRequest();
		vehicleRequest.setLicensePlate("ABC-123");
		vehicleRequest.setOwnerName("John");
		vehicleRequest.setType("CAR");

		VehicleType vehicleType = new VehicleType();
		vehicleType.setName("CAR");

		when(vehicleTypeRepository.findByNameIgnoreCase("CAR"))
				.thenReturn(Optional.of(vehicleType));

		when(vehicleRepository.save(any(Vehicle.class)))
				.thenThrow(new DataIntegrityViolationException("dup"));

		ResourceAlreadyExistsException ex = assertThrows(
				ResourceAlreadyExistsException.class,
				() -> service.registerVehicle(vehicleRequest)
		);

		assertTrue(ex.getMessage().contains("already exists"));
	}
}
