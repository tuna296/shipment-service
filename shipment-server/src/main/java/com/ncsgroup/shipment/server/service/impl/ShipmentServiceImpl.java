package com.ncsgroup.shipment.server.service.impl;

import com.ncsgroup.shipment.server.dto.shipment.ShipmentResponse;
import com.ncsgroup.shipment.server.entity.Shipment;
import com.ncsgroup.shipment.server.exception.shipment.ShipmentNotFoundException;
import com.ncsgroup.shipment.server.repository.ShipmentRepository;
import com.ncsgroup.shipment.server.service.ShipmentService;
import com.ncsgroup.shipment.server.service.base.BaseServiceImpl;
import com.ncsgroup.shipment.client.dto.ShipmentRequest;
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

  @Override
  public ShipmentResponse update(ShipmentRequest request, String id) {
    log.info("(update) request: {} id: {}", request, id);
    Shipment shipment = this.find(id);
    this.setValueForUpdate(request, shipment);
    repository.saveAndFlush(shipment);
    return repository.find(shipment.getId());
  }

  @Override
  public void delete(String id) {
    log.info("(delete) id: {}", id);
    this.find(id);
    repository.delete(id);
  }

  @Override
  public ShipmentResponse detail(String id) {
    log.info("(detail) id: {}", id);
    Shipment shipment = this.find(id);
    return new ShipmentResponse(
          shipment.getId(),
          shipment.getCode(),
          shipment.getPrice(),
          shipment.getShipmentMethodId(),
          shipment.getFromAddressId(),
          shipment.getToAddressId()
    );
  }

  private Shipment find(String id) {
    log.info("(update)id: {}", id);
    Shipment shipment = repository.findById(id).orElseThrow(ShipmentNotFoundException::new);
    if (shipment.isDeleted()) {
      throw new ShipmentNotFoundException();
    }
    return shipment;
  }

  private void setValueForUpdate(ShipmentRequest request, Shipment shipment) {
    log.info("(setValueForUpdate) request: {}, shipment: {}", request, shipment);

    shipment.setShipmentMethodId(request.getShipmentMethodId());
    shipment.setOrderId(request.getOrderId());
    shipment.setFromAddressId(request.getFromAddressId());
    shipment.setToAddressId(request.getToAddressId());
    shipment.setShipmentMethodId(request.getShipmentMethodId());
    shipment.setPrice(request.getPrice());
  }
}
