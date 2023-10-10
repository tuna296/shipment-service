package com.ncsgroup.shipment.server.exception.shipmentmethod;

import com.ncsgroup.shipment.server.exception.base.BadRequestException;

public class ShipmentMethodAlreadyExistException extends BadRequestException {
    public ShipmentMethodAlreadyExistException() {
        setCode("com.ncsgroup.shipment.server.exception.shipmentmethod.ShipmentAlreadyExistException");
    }
}
