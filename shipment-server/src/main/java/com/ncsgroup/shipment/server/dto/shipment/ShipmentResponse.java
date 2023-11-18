package com.ncsgroup.shipment.server.dto.shipment;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ncsgroup.shipment.server.dto.address.AddressResponse;
import com.ncsgroup.shipment.server.entity.ShipmentMethod;
import com.ncsgroup.shipment.server.entity.enums.ShipmentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)

public class ShipmentResponse {
  private String id;
  private String code;
  private AddressResponse fromAddress;
  private AddressResponse toAddress;
  private Double price;
  private ShipmentMethod shipmentMethod;
  private long shipmentExpectedDate;
  private ShipmentStatus shipmentStatus;

}
