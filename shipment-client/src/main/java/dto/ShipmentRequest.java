package dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import static constants.Constants.Validate.*;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Data
@AllArgsConstructor
@NoArgsConstructor

public class ShipmentRequest {
  @NotBlank(message = ADDRESS_NOT_BLANK)
  private String fromAddressId;

  @NotBlank(message = ADDRESS_NOT_BLANK)
  private String toAddressId;

  @DecimalMin(value = "0", message = CHECK_PRICE_SHIPMENT)
  @NotNull(message = PRICE_SHIPMENT_NOT_NULL)
  private Double price;

  @NotBlank(message = SHIPMENT_METHOD_BLANK)
  private String shipmentMethodId;
}
