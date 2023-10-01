package dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
    public class ShipmentMethodRequest {
    @NotBlank(message = "name cannot be empty")
    private String name;
    @NotBlank(message = "description cannot be empty")
    private String description;
    @NotBlank(message = "pricePerKilometer cannot be empty")
    private double pricePerKilometer;
}
