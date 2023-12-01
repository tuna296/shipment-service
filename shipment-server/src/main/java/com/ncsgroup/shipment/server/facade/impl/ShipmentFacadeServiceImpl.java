package com.ncsgroup.shipment.server.facade.impl;

import com.ncsgroup.shipment.server.dto.address.AddressResponse;
import com.ncsgroup.shipment.server.dto.shipment.ShipmentResponse;
import com.ncsgroup.shipment.server.dto.shipmentmethod.ShipmentMethodResponse;
import com.ncsgroup.shipment.server.facade.ShipmentFacadeService;
import com.ncsgroup.shipment.server.service.ShipmentMethodService;
import com.ncsgroup.shipment.server.service.ShipmentService;
import com.ncsgroup.shipment.server.service.address.AddressService;
import com.ncsgroup.shipment.client.dto.ShipmentRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@RequiredArgsConstructor
@Slf4j
public class ShipmentFacadeServiceImpl implements ShipmentFacadeService {
  private final ShipmentService shipmentService;
  private final AddressService addressService;
  private final ShipmentMethodService shipmentMethodService;

  @Override
  @Transactional
  public ShipmentResponse create(ShipmentRequest request) {
    log.info("(create) request: {}", request);
    this.checkShipmentRequest(
          request.getFromAddressId(),
          request.getToAddressId(),
          request.getShipmentMethodId()
    );

    ShipmentResponse response = shipmentService.create(request);
    AddressResponse fromAddress = addressService.detail(request.getFromAddressId());
    AddressResponse toAddress = addressService.detail(request.getToAddressId());
    ShipmentMethodResponse shipmentMethod = shipmentMethodService.detail(request.getShipmentMethodId());

    response.setFromAddress(fromAddress);
    response.setToAddress(toAddress);
    response.setShipmentMethod(shipmentMethod);

    return response;
  }


  @Override
  public ShipmentResponse update(ShipmentRequest request, String id) {
    log.debug("(request) update, request: {}, id: {}", request, id);
    this.checkShipmentRequest(
          request.getFromAddressId(),
          request.getToAddressId(),
          request.getShipmentMethodId()
    );
    ShipmentResponse response = shipmentService.update(request, id);
    AddressResponse fromAddress = addressService.detail(request.getFromAddressId());
    AddressResponse toAddress = addressService.detail(request.getToAddressId());
    ShipmentMethodResponse shipmentMethod = shipmentMethodService.detail(request.getShipmentMethodId());

    response.setFromAddress(fromAddress);
    response.setToAddress(toAddress);
    response.setShipmentMethod(shipmentMethod);

    return response;
  }

  @Override
  public ShipmentResponse detail(String id) {
    log.debug("(detail) id: {}", id);
    ShipmentResponse response = shipmentService.detail(id);
    this.checkShipmentRequest(
          response.getFromAddress().getId(),
          response.getToAddress().getId(),
          response.getShipmentMethod().getId()
    );
    response.setFromAddress(addressService.detail(response.getFromAddress().getId()));
    response.setToAddress(addressService.detail(response.getToAddress().getId()));
    response.setShipmentMethod(shipmentMethodService.detail(response.getShipmentMethod().getId()));
    return response;
  }

  private void checkShipmentRequest(String fromAddressId, String toAddressId, String shipmentMethodId) {
    log.debug("checkShipmentRequest, fromAddressId {}, toAddressId {}, shipmentMethodId {}",
          fromAddressId, toAddressId, shipmentMethodId);

    if (Objects.nonNull(fromAddressId)) {
      addressService.detail(fromAddressId);
    }

    if (Objects.nonNull(toAddressId)) {
      addressService.detail(toAddressId);
    }

    if (Objects.nonNull(shipmentMethodId)) {
      shipmentMethodService.detail(shipmentMethodId);
    }
  }

}
