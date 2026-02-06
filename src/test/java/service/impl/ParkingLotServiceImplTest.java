package service.impl;

import dev.fjcodes.exception.ResourceAlreadyExistsException;
import dev.fjcodes.exception.ResourceNotFoundException;
import dev.fjcodes.model.ParkingLot;
import dev.fjcodes.model.Vehicle;
import dev.fjcodes.model.VehicleType;
import dev.fjcodes.repository.ParkingLotRepository;
import dev.fjcodes.repository.VehicleRepository;
import dev.fjcodes.request.ParkingLotRequest;
import dev.fjcodes.response.ParkingLotResponse;
import dev.fjcodes.response.VehicleResponse;
import dev.fjcodes.service.impl.ParkingLotServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ParkingLotServiceImplTest {
	private ParkingLotRepository parkingLotRepository;

	private VehicleRepository vehicleRepository;

	private ParkingLotServiceImpl service;

	@BeforeEach
	void setup() {
		parkingLotRepository = mock(ParkingLotRepository.class);
		vehicleRepository = mock(VehicleRepository.class);
		service = new ParkingLotServiceImpl(parkingLotRepository, vehicleRepository);
	}

	// ---------------- registerParkingLot ----------------

	@Test
	void registerParkingLot_success() {
		ParkingLotRequest parkingLotRequest = new ParkingLotRequest();
		parkingLotRequest.setLotId("LOT-1");
		parkingLotRequest.setLocation("LOC");
		parkingLotRequest.setCapacity(10);
		parkingLotRequest.setOccupiedSpaces(2);

		ParkingLot savedParkingLot = new ParkingLot();
		savedParkingLot.setLotId("LOT-1");
		savedParkingLot.setLocation("LOC");
		savedParkingLot.setCapacity(10);
		savedParkingLot.setOccupiedSpaces(2);

		when(parkingLotRepository.save(any(ParkingLot.class))).thenReturn(savedParkingLot);

		ParkingLotResponse parkingLotResponse = service.registerParkingLot(parkingLotRequest);

		assertEquals("LOT-1", parkingLotResponse.getLotId());
		assertEquals("LOC", parkingLotResponse.getLocation());
		assertEquals(10, parkingLotResponse.getCapacity());
		assertEquals(2, parkingLotResponse.getOccupiedSpaces());
	}

	@Test
	void registerParkingLot_invalidCapacity() {
		ParkingLotRequest parkingLotRequest = new ParkingLotRequest();
		parkingLotRequest.setCapacity(5);
		parkingLotRequest.setOccupiedSpaces(6);

		IllegalArgumentException ex = assertThrows(
				IllegalArgumentException.class,
				() -> service.registerParkingLot(parkingLotRequest)
		);

		assertEquals("Occupied Spaces cannot be more than the capacity", ex.getMessage());
	}

	@Test
	void registerParkingLot_duplicateLotId() {
		ParkingLotRequest parkingLotRequest = new ParkingLotRequest();
		parkingLotRequest.setLotId("LOT-1");
		parkingLotRequest.setCapacity(10);
		parkingLotRequest.setOccupiedSpaces(1);

		when(parkingLotRepository.save(any(ParkingLot.class)))
				.thenThrow(new DataIntegrityViolationException("dup"));

		ResourceAlreadyExistsException ex = assertThrows(
				ResourceAlreadyExistsException.class,
				() -> service.registerParkingLot(parkingLotRequest)
		);

		assertTrue(ex.getMessage().contains("already exists"));
	}

	// ---------------- getStatusOfParkingLot ----------------

	@Test
	void getStatusOfParkingLot_success() {
		ParkingLot parkingLot = new ParkingLot();
		parkingLot.setLotId("LOT-1");
		parkingLot.setLocation("LOC");
		parkingLot.setCapacity(10);
		parkingLot.setOccupiedSpaces(3);

		when(parkingLotRepository.findByLotId("LOT-1")).thenReturn(Optional.of(parkingLot));

		ParkingLotResponse statusOfParkingLot = service.getStatusOfParkingLot("LOT-1");

		assertEquals("LOT-1", statusOfParkingLot.getLotId());
		assertEquals("LOC", statusOfParkingLot.getLocation());
		assertEquals(10, statusOfParkingLot.getCapacity());
		assertEquals(3, statusOfParkingLot.getOccupiedSpaces());
	}

	@Test
	void getStatusOfParkingLot_notFound() {
		when(parkingLotRepository.findByLotId("LOT-1")).thenReturn(Optional.empty());

		ResourceNotFoundException ex = assertThrows(
				ResourceNotFoundException.class,
				() -> service.getStatusOfParkingLot("LOT-1")
		);

		assertTrue(ex.getMessage().contains("not found"));
	}

	// ---------------- getVehiclesFromParkingLot ----------------

	@Test
	void getVehiclesFromParkingLot_success() {
		when(parkingLotRepository.findByLotId("LOT-1"))
				.thenReturn(Optional.of(new ParkingLot()));

		VehicleType vehicleType = new VehicleType();
		vehicleType.setName("CAR");

		Vehicle v = new Vehicle();
		v.setLicensePlate("ABC");
		v.setOwnerName("John");
		v.setVehicleType(vehicleType);

		when(vehicleRepository.findByParkingLot_LotId("LOT-1"))
				.thenReturn(Collections.singletonList(v));

		List<VehicleResponse> vehiclesFromParkingLot = service.getVehiclesFromParkingLot("LOT-1");

		assertEquals(1, vehiclesFromParkingLot.size());
		assertEquals("ABC", vehiclesFromParkingLot.get(0).getLicensePlate());
		assertEquals("John", vehiclesFromParkingLot.get(0).getOwnerName());
		assertEquals("CAR", vehiclesFromParkingLot.get(0).getType());
	}

	@Test
	void getVehiclesFromParkingLot_emptyList() {
		when(parkingLotRepository.findByLotId("LOT-1"))
				.thenReturn(Optional.of(new ParkingLot()));

		when(vehicleRepository.findByParkingLot_LotId("LOT-1"))
				.thenReturn(Collections.emptyList());

		List<VehicleResponse> vehiclesFromParkingLot = service.getVehiclesFromParkingLot("LOT-1");

		assertNotNull(vehiclesFromParkingLot);
		assertTrue(vehiclesFromParkingLot.isEmpty());
	}

	@Test
	void getVehiclesFromParkingLot_parkingLotNotFound() {
		when(parkingLotRepository.findByLotId("LOT-1"))
				.thenReturn(Optional.empty());

		ResourceNotFoundException ex = assertThrows(
				ResourceNotFoundException.class,
				() -> service.getVehiclesFromParkingLot("LOT-1")
		);

		assertTrue(ex.getMessage().contains("not found"));
	}
}
