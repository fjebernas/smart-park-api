package service.impl;

import dev.fjcodes.exception.IllegalCheckInOutException;
import dev.fjcodes.exception.ResourceNotFoundException;
import dev.fjcodes.model.ParkingLot;
import dev.fjcodes.model.Vehicle;
import dev.fjcodes.repository.ParkingLotRepository;
import dev.fjcodes.repository.VehicleRepository;
import dev.fjcodes.service.impl.CheckInOutServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CheckInOutServiceImplTest {
	private ParkingLotRepository parkingLotRepository;

	private VehicleRepository vehicleRepository;

	private CheckInOutServiceImpl service;

	private ParkingLot parkingLot;

	private Vehicle vehicle;

	@BeforeEach
	void setup() {
		parkingLotRepository = mock(ParkingLotRepository.class);
		vehicleRepository = mock(VehicleRepository.class);
		service = new CheckInOutServiceImpl(parkingLotRepository, vehicleRepository);

		parkingLot = new ParkingLot();
		parkingLot.setLotId("LOT-1");
		parkingLot.setCapacity(2);
		parkingLot.setOccupiedSpaces(0);

		vehicle = new Vehicle();
		vehicle.setLicensePlate("ABC-123");
		vehicle.setParkingLot(null);
	}

	// ---------------- CHECK IN ----------------

	@Test
	void checkIn_success() {
		when(parkingLotRepository.findByLotId("LOT-1")).thenReturn(Optional.of(parkingLot));
		when(vehicleRepository.findByLicensePlate("ABC-123")).thenReturn(Optional.of(vehicle));

		service.checkInVehicle("LOT-1", "ABC-123");

		assertEquals(1, parkingLot.getOccupiedSpaces());
		assertEquals(parkingLot, vehicle.getParkingLot());

		verify(parkingLotRepository).save(parkingLot);
		verify(vehicleRepository).save(vehicle);
	}

	@Test
	void checkIn_parkingLotNotFound() {
		when(parkingLotRepository.findByLotId("LOT-1")).thenReturn(Optional.empty());

		ResourceNotFoundException ex = assertThrows(
				ResourceNotFoundException.class,
				() -> service.checkInVehicle("LOT-1", "ABC-123")
		);

		assertTrue(ex.getMessage().contains("Parking Lot with Lot ID"));
	}

	@Test
	void checkIn_vehicleNotFound() {
		when(parkingLotRepository.findByLotId("LOT-1")).thenReturn(Optional.of(parkingLot));
		when(vehicleRepository.findByLicensePlate("ABC-123")).thenReturn(Optional.empty());

		ResourceNotFoundException ex = assertThrows(
				ResourceNotFoundException.class,
				() -> service.checkInVehicle("LOT-1", "ABC-123")
		);

		assertTrue(ex.getMessage().contains("Vehicle with plate"));
	}

	@Test
	void checkIn_vehicleAlreadyParked() {
		ParkingLot otherParkingLot = new ParkingLot();
		otherParkingLot.setLotId("LOT-X");
		vehicle.setParkingLot(otherParkingLot);

		when(parkingLotRepository.findByLotId("LOT-1")).thenReturn(Optional.of(parkingLot));
		when(vehicleRepository.findByLicensePlate("ABC-123")).thenReturn(Optional.of(vehicle));

		IllegalCheckInOutException ex = assertThrows(
				IllegalCheckInOutException.class,
				() -> service.checkInVehicle("LOT-1", "ABC-123")
		);

		assertEquals("Vehicle already parked", ex.getMessage());
	}

	@Test
	void checkIn_parkingLotFull() {
		parkingLot.setCapacity(1);
		parkingLot.setOccupiedSpaces(1);

		when(parkingLotRepository.findByLotId("LOT-1")).thenReturn(Optional.of(parkingLot));
		when(vehicleRepository.findByLicensePlate("ABC-123")).thenReturn(Optional.of(vehicle));

		IllegalCheckInOutException ex = assertThrows(
				IllegalCheckInOutException.class,
				() -> service.checkInVehicle("LOT-1", "ABC-123")
		);

		assertEquals("Parking lot full", ex.getMessage());
	}

	// ---------------- CHECK OUT ----------------

	@Test
	void checkOut_success() {
		vehicle.setParkingLot(parkingLot);
		parkingLot.setOccupiedSpaces(1);

		when(parkingLotRepository.findByLotId("LOT-1")).thenReturn(Optional.of(parkingLot));
		when(vehicleRepository.findByLicensePlate("ABC-123")).thenReturn(Optional.of(vehicle));

		service.checkOutVehicle("LOT-1", "ABC-123");

		assertEquals(0, parkingLot.getOccupiedSpaces());
		assertNull(vehicle.getParkingLot());

		verify(parkingLotRepository).save(parkingLot);
		verify(vehicleRepository).save(vehicle);
	}

	@Test
	void checkOut_parkingLotNotFound() {
		when(parkingLotRepository.findByLotId("LOT-1")).thenReturn(Optional.empty());

		ResourceNotFoundException ex = assertThrows(
				ResourceNotFoundException.class,
				() -> service.checkOutVehicle("LOT-1", "ABC-123")
		);

		assertTrue(ex.getMessage().contains("Parking lot with Lot ID"));
	}

	@Test
	void checkOut_vehicleNotFound() {
		when(parkingLotRepository.findByLotId("LOT-1")).thenReturn(Optional.of(parkingLot));
		when(vehicleRepository.findByLicensePlate("ABC-123")).thenReturn(Optional.empty());

		ResourceNotFoundException ex = assertThrows(
				ResourceNotFoundException.class,
				() -> service.checkOutVehicle("LOT-1", "ABC-123")
		);

		assertTrue(ex.getMessage().contains("Vehicle with plate"));
	}

	@Test
	void checkOut_vehicleNotInAnyLot() {
		vehicle.setParkingLot(null);

		when(parkingLotRepository.findByLotId("LOT-1")).thenReturn(Optional.of(parkingLot));
		when(vehicleRepository.findByLicensePlate("ABC-123")).thenReturn(Optional.of(vehicle));

		IllegalCheckInOutException ex = assertThrows(
				IllegalCheckInOutException.class,
				() -> service.checkOutVehicle("LOT-1", "ABC-123")
		);

		assertEquals("Vehicle not in this Parking Lot", ex.getMessage());
	}

	@Test
	void checkOut_vehicleInDifferentLot() {
		ParkingLot otherParkingLot = new ParkingLot();
		otherParkingLot.setLotId("LOT-X");
		vehicle.setParkingLot(otherParkingLot);

		when(parkingLotRepository.findByLotId("LOT-1")).thenReturn(Optional.of(parkingLot));
		when(vehicleRepository.findByLicensePlate("ABC-123")).thenReturn(Optional.of(vehicle));

		IllegalCheckInOutException ex = assertThrows(
				IllegalCheckInOutException.class,
				() -> service.checkOutVehicle("LOT-1", "ABC-123")
		);

		assertEquals("Vehicle not in this Parking Lot", ex.getMessage());
	}
}
