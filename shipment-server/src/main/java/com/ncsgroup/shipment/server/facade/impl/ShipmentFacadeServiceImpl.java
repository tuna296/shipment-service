package com.ncsgroup.shipment.server.facade.impl;

import com.ncsgroup.shipment.server.dto.address.AddressResponse;
import com.ncsgroup.shipment.server.dto.shipment.ShipmentResponse;
import com.ncsgroup.shipment.server.dto.shipmentmethod.ShipmentMethodResponse;
import com.ncsgroup.shipment.server.exception.address.AddressNotFoundException;
import com.ncsgroup.shipment.server.exception.shipmentmethod.ShipmentMethodNotFoundException;
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
    try {
      ShipmentMethodResponse shipmentMethodResponse =
            shipmentMethodService.detail(response.getShipmentMethod().getId());
      response.setShipmentMethod(shipmentMethodResponse);

    } catch (ShipmentMethodNotFoundException e) {
      response.setShipmentMethod(null);
      log.error("(detail) ======> ShipmentMethodNotFoundException");
    }

    try {
      AddressResponse fromAddressResponse = addressService.detail(response.getFromAddress().getId());
      response.setFromAddress(fromAddressResponse);
    } catch (AddressNotFoundException e) {
      response.setFromAddress(null);
      log.error("(detail) ======> AddressNotFoundException");
    }

    try {
      AddressResponse toAddressResponse = addressService.detail(response.getToAddress().getId());
      response.setToAddress(toAddressResponse);
    } catch (AddressNotFoundException e) {
      response.setToAddress(null);
      log.error("(detail) ======> AddressNotFoundException");
    }
    return response;
  }

}
