package com.ncsgroup.shipment.server.exception.shipment;

import com.ncsgroup.shipment.server.exception.base.BadRequestException;

public class ShipmentNotFoundException extends BadRequestException {
  public ShipmentNotFoundException() {
    setCode("com.ncsgroup.shipment.server.exception.shipment.ShipmentNotFoundException");
  }
}
