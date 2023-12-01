package com.ncsgroup.shipment.server.exception.shipment;

import com.ncsgroup.shipment.server.exception.base.NotFoundException;

public class ShipmentNotFoundException extends NotFoundException {
  public ShipmentNotFoundException() {
    setCode("com.ncsgroup.shipment.server.exception.shipment.ShipmentNotFoundException");
  }
}
