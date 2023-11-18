package com.ncsgroup.shipment.server.dto.shipmentmethod;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ShipmentMethodResponse {
  private String id;
  private String name;
  private String description;
  private double pricePerKilometer;

  public static ShipmentMethodResponse from(
        String name,
        String description,
        double pricePerKilometer
  ) {
    return new ShipmentMethodResponse(null, name, description, pricePerKilometer);
  }

}
