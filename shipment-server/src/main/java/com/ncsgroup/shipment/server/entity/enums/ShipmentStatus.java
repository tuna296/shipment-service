package com.ncsgroup.shipment.server.entity.enums;

import lombok.Getter;

@Getter
public enum ShipmentStatus {
  CONFIRMING(0),
  CONFIRMED(1),
  IN_TRANSIT(2),
  DELIVERED(3),
  COMPLETED(4),
  CANCELLED(5);
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

  public static ShipmentStatus valueOf(int value) {
    for (ShipmentStatus shipmentStatus : values()) {
      if (shipmentStatus.getValue() == value) {
        return shipmentStatus;
      }
    }
    throw new IllegalArgumentException("Invalid ThresholdTime value: " + value);
  }

  public int getValue() {
    return value;
  }

}
