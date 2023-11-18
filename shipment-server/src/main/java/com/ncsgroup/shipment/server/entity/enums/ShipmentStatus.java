package com.ncsgroup.shipment.server.entity.enums;

import lombok.Getter;

@Getter
public enum ShipmentStatus {
  DELIVERED(0),
  IN_TRANSIT(1),
  COMPLETED(2),
  CANCELLED(3);
  private final int value;

  ShipmentStatus(int value) {
    this.value = value;
  }

  public static ShipmentStatus of(int value) {
    for (ShipmentStatus status : ShipmentStatus.values()) {
      if (status.getValue() == value) {
        return status;
      }
    }
    return null;
  }
}
