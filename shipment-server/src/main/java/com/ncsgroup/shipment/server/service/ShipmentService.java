package com.ncsgroup.shipment.server.service;

import com.ncsgroup.shipment.server.dto.shipment.ShipmentResponse;
import com.ncsgroup.shipment.server.entity.Shipment;
import com.ncsgroup.shipment.server.service.base.BaseService;
import dto.ShipmentRequest;

public interface ShipmentService extends BaseService<Shipment> {
  ShipmentResponse create(ShipmentRequest request);

  ShipmentResponse update(ShipmentRequest request, String id);
}
