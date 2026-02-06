package dev.fjcodes.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class VehicleRequest {
	@Pattern(regexp = "^[A-Za-z0-9-]+$", message = "Only letters, numbers, and dashes are allowed")
	@NotBlank
	private String licensePlate;

	@Pattern(regexp = "^[A-Za-z ]+$", message = "Only letters and spaces are allowed")
	@NotBlank
	private String ownerName;

	@NotEmpty
	private String type;
}
