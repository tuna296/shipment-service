package com.ncsgroup.shipment.server.service;

import com.ncsgroup.shipment.server.dto.shipmentmethod.ShipmentMethodPageResponse;
import com.ncsgroup.shipment.server.dto.shipmentmethod.ShipmentMethodResponse;
import com.ncsgroup.shipment.server.entity.ShipmentMethod;
import com.ncsgroup.shipment.server.service.base.BaseService;
import dto.ShipmentMethodRequest;

public interface ShipmentMethodService extends BaseService<ShipmentMethod> {
  ShipmentMethodResponse create(ShipmentMethodRequest request);

  ShipmentMethodResponse update(String id, ShipmentMethodRequest request);

  ShipmentMethodPageResponse list(String keyword, int size, int page, boolean isAll);

  void delete(String id);
}
