package com.ncsgroup.shipment.server.dto.shipment;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ncsgroup.shipment.server.dto.address.AddressResponse;
import com.ncsgroup.shipment.server.dto.shipmentmethod.ShipmentMethodResponse;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)

public class ShipmentResponse {
  private String id;
  private String code;
  private Double price;
  private ShipmentMethodResponse shipmentMethod;
  private AddressResponse fromAddress;
  private AddressResponse toAddress;

  public ShipmentResponse(String id, String code, Double price) {
    this.id = id;
    this.code = code;
    this.price = price;
  }
}
