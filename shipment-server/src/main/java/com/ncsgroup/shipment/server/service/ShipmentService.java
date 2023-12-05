package com.ncsgroup.shipment.server.service;

import com.ncsgroup.shipment.server.dto.PageResponse;
import com.ncsgroup.shipment.server.dto.shipment.ShipmentResponse;
import com.ncsgroup.shipment.server.entity.Shipment;
import com.ncsgroup.shipment.server.service.base.BaseService;
import com.ncsgroup.shipment.client.dto.ShipmentRequest;

public interface ShipmentService extends BaseService<Shipment> {
  ShipmentResponse create(ShipmentRequest request);

  ShipmentResponse update(ShipmentRequest request, String id);

  void delete(String id);

  ShipmentResponse detail(String id);

  PageResponse<ShipmentResponse> list(String keyword, int size, int page, boolean isAll);

}
