package com.ncsgroup.shipment.server.service.impl;

import com.ncsgroup.shipment.server.dto.shipment.ShipmentResponse;
import com.ncsgroup.shipment.server.entity.Shipment;
import com.ncsgroup.shipment.server.repository.ShipmentRepository;
import com.ncsgroup.shipment.server.service.ShipmentService;
import com.ncsgroup.shipment.server.service.base.BaseServiceImpl;
import dto.ShipmentRequest;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ShipmentServiceImpl extends BaseServiceImpl<Shipment> implements ShipmentService {
  private final ShipmentRepository repository;

  public ShipmentServiceImpl(ShipmentRepository repository) {
    super(repository);
    this.repository = repository;
  }

  @Override
  @Transactional
  public ShipmentResponse create(ShipmentRequest request) {
    log.info("(request) create: {}", request);
    Shipment shipment = new Shipment(
          request.getOrderId(),
          request.getFromAddressId(),
          request.getToAddressId(),
          request.getPrice(),
          request.getShipmentMethodId()
    );
    repository.saveAndFlush(shipment);
    return repository.find(shipment.getId());
  }
}
