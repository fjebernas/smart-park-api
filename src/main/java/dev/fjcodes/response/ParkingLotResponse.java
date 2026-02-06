package dev.fjcodes.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ParkingLotResponse {
	private String lotId;

	private String location;

	private int capacity;

	private int occupiedSpaces;
}
