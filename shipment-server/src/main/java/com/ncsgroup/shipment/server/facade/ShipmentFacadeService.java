package com.ncsgroup.shipment.server.facade;

import com.ncsgroup.shipment.server.dto.shipment.ShipmentResponse;
import com.ncsgroup.shipment.client.dto.ShipmentRequest;

public interface ShipmentFacadeService {
  ShipmentResponse create(ShipmentRequest request);

  ShipmentResponse update(ShipmentRequest request, String id);
}
