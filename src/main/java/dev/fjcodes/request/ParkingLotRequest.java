package dev.fjcodes.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;

@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ParkingLotRequest {
	@NotBlank
	@Size(max = 50)
	private String lotId;

	@NotBlank
	private String location;

	@Positive
	private int capacity;

	@PositiveOrZero
	private int occupiedSpaces;
}
