package com.ncsgroup.shipment.server.entity;

import com.ncsgroup.shipment.server.entity.base.BaseEntityWithUpdater;
import com.ncsgroup.shipment.server.entity.enums.ShipmentStatus;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "shipments")
@AllArgsConstructor
@NoArgsConstructor
public class Shipment extends BaseEntityWithUpdater {
  @Column(name = "code")
  private String code;
  @Column(name = "from_address_id")
  private String fromAddressId;
  @Column(name = "to_address_id")
  private String toAddressId;
  @Column(name = "price")
  private Double price;
  @Column(name = "shipment_method_id")
  private String shipmentMethodId;
  @Column(name = "shipment_expected_date")
  private long shipmentExpectedDate;
  @Enumerated(EnumType.ORDINAL)
  @Column(name = "shipment_status")
  private ShipmentStatus shipmentStatus;

  public Shipment(
        String fromAddressId,
        String toAddressId,
        Double price,
        String shipmentMethodId
  ) {
    this.fromAddressId = fromAddressId;
    this.toAddressId = toAddressId;
    this.price = price;
    this.shipmentMethodId = shipmentMethodId;
    shipmentStatus = ShipmentStatus.DELIVERED;
  }
}
