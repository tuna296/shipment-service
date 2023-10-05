package com.ncsgroup.shipment.server.exception.shipmentmethod;

import com.ncsgroup.shipment.server.exception.base.BadRequestException;

public class ShipmentAlreadyExistException extends BadRequestException {
    public ShipmentAlreadyExistException() {
        setCode("com.ncsgroup.shipment.server.exception.shipmentmethod.ShipmentAlreadyExistException");
    }
}
