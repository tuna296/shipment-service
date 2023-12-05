package com.ncsgroup.shipment.server.dto.shipment;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ncsgroup.shipment.server.dto.address.AddressResponse;
import com.ncsgroup.shipment.server.dto.shipmentmethod.ShipmentMethodResponse;
import lombok.*;
import com.fasterxml.jackson.annotation.JsonInclude;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
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

  public ShipmentResponse(
        String id, String code, Double price,
        String shipmentMethodId,
        String fromAddressId,
        String toAddressId
  ) {
    this.id = id;
    this.code = code;
    this.price = price;
    this.shipmentMethod = new ShipmentMethodResponse(shipmentMethodId, null, null, 0);
    this.fromAddress = new AddressResponse(fromAddressId, null, null, null, null);
    this.toAddress = new AddressResponse(toAddressId, null, null, null, null);
  }
}
