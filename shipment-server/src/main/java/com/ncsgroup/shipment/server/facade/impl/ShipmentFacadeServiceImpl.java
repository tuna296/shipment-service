package com.ncsgroup.shipment.server.facade.impl;

import com.ncsgroup.shipment.server.dto.address.AddressResponse;
import com.ncsgroup.shipment.server.dto.shipment.ShipmentResponse;
import com.ncsgroup.shipment.server.dto.shipmentmethod.ShipmentMethodResponse;
import com.ncsgroup.shipment.server.facade.ShipmentFacadeService;
import com.ncsgroup.shipment.server.service.ShipmentMethodService;
import com.ncsgroup.shipment.server.service.ShipmentService;
import com.ncsgroup.shipment.server.service.address.AddressService;
import dto.ShipmentRequest;
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
    this.checkShipmentRequest(request);

    ShipmentResponse response = shipmentService.create(request);
    AddressResponse fromAddress = addressService.detail(request.getFromAddressId());
    AddressResponse toAddress = addressService.detail(request.getToAddressId());
    ShipmentMethodResponse shipmentMethod = shipmentMethodService.detail(request.getShipmentMethodId());

    response.setFromAddress(fromAddress);
    response.setToAddress(toAddress);
    response.setShipmentMethod(shipmentMethod);

    return response;
  }

  private void checkShipmentRequest(ShipmentRequest request) {
    log.debug("checkShipmentRequest: {}", request);
    if (Objects.nonNull(request.getToAddressId())) {
      addressService.detail(request.getToAddressId());
    }
    if (Objects.nonNull(request.getFromAddressId())) {
      addressService.detail(request.getFromAddressId());
    }
    if (Objects.nonNull(request.getShipmentMethodId())) {
      shipmentMethodService.detail(request.getShipmentMethodId());
    }
  }
}
