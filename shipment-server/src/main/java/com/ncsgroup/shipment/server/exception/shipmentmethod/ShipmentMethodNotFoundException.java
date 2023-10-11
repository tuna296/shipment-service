package com.ncsgroup.shipment.server.exception.shipmentmethod;

import com.ncsgroup.shipment.server.exception.base.NotFoundException;

public class ShipmentMethodNotFoundException extends NotFoundException {
  public ShipmentMethodNotFoundException() {
    setCode("com.ncsgroup.shipment.server.exception.shipmentmethod.ShipmentMethodNotFoundException");
  }
}
