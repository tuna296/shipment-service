package com.ncsgroup.shipment.server.service.impl;

import com.ncsgroup.shipment.server.dto.shipmentmethod.ShipmentMethodPageResponse;
import com.ncsgroup.shipment.server.dto.shipmentmethod.ShipmentMethodResponse;
import com.ncsgroup.shipment.server.entity.ShipmentMethod;
import com.ncsgroup.shipment.server.exception.shipmentmethod.ShipmentMethodAlreadyExistException;
import com.ncsgroup.shipment.server.exception.shipmentmethod.ShipmentMethodNotFoundException;
import com.ncsgroup.shipment.server.repository.ShipmentMethodRepository;
import com.ncsgroup.shipment.server.service.ShipmentMethodService;
import com.ncsgroup.shipment.server.service.base.BaseServiceImpl;
import dto.ShipmentMethodRequest;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ShipmentMethodServiceImpl extends BaseServiceImpl<ShipmentMethod> implements ShipmentMethodService {
  private final ShipmentMethodRepository repository;

  public ShipmentMethodServiceImpl(ShipmentMethodRepository repository) {
    super(repository);
    this.repository = repository;
  }

  @Override
  @Transactional
  public ShipmentMethodResponse create(ShipmentMethodRequest request) {
    log.info("(create) request: {}", request);
    this.checkShipmentMethodAlreadyExists(request.getName());
    ShipmentMethod shipmentMethod = ShipmentMethod.from(
          request.getName(),
          request.getDescription(),
          request.getPricePerKilometer()
    );
    create(shipmentMethod);
    return convertToResponse(shipmentMethod);
  }

  @Override
  @Transactional
  public ShipmentMethodResponse update(String id, ShipmentMethodRequest request) {
    log.info("(update) request: {}", request);

    ShipmentMethod shipmentMethod = findById(id);
    checkNameShipmentMethodAlreadyExists(shipmentMethod, request);
    setValueUpdate(shipmentMethod, request);
    shipmentMethod = update(shipmentMethod);
    return convertToResponse(shipmentMethod);
  }

  @Override
  public ShipmentMethodPageResponse list(String keyword, int size, int page, boolean isAll) {
    log.info("(list) keyword: {}, size : {}, page: {}, isAll: {}", keyword, size, page, isAll);
    List<ShipmentMethodResponse> list = new ArrayList<>();
    Pageable pageable = PageRequest.of(page, size);
    List<ShipmentMethod> shipmentMethods = isAll ?
          repository.findAll() : repository.search(keyword, pageable);
    for (ShipmentMethod shipmentMethod : shipmentMethods) {
      list.add(convertToResponse(shipmentMethod));
    }
    return new ShipmentMethodPageResponse(list, isAll ? shipmentMethods.size() : repository.countSearch(keyword));
  }

  @Override
  @Transactional
  public void delete(String id) {
    log.info("delete by id " + id);
    checkAlreadyById(id);
    repository.deleteById(id);
  }

  private ShipmentMethod findById(String id) {
    log.debug("(findById) id: {}", id);
    ShipmentMethod shipmentMethod = repository.findById(id).orElseThrow(ShipmentMethodNotFoundException::new);
    if (shipmentMethod.isDeleted())
      throw new ShipmentMethodNotFoundException();
    return shipmentMethod;
  }

  private void checkShipmentMethodAlreadyExists(String name) {
    log.debug("checkShipmentMethodAlreadyExists :{}", name);
    if (repository.existsByName(name)) {
      log.error("Shipment Method AlreadyExists:{}, name");
      throw new ShipmentMethodAlreadyExistException();
    }
  }

  private void checkNameShipmentMethodAlreadyExists(ShipmentMethod shipmentMethod, ShipmentMethodRequest request) {
    log.debug("check name of shipment method AlreadyExists when update");
    if (!shipmentMethod.getName().equals(request.getName()))
      this.checkShipmentMethodAlreadyExists(request.getName());
  }

  private void setValueUpdate(ShipmentMethod shipmentmethod, ShipmentMethodRequest request) {
    shipmentmethod.setName(request.getName());
    shipmentmethod.setDescription(request.getDescription());
    shipmentmethod.setPricePerKilometer(request.getPricePerKilometer());
  }

  private ShipmentMethodResponse convertToResponse(ShipmentMethod shipmentMethod) {
    return ShipmentMethodResponse.from(
          shipmentMethod.getName(),
          shipmentMethod.getDescription(),
          shipmentMethod.getPricePerKilometer());
  }

  private void checkAlreadyById(String id) {
    log.debug("Checking existing shipment by id " + id);
    if (!repository.existsById(id)) {
      throw new ShipmentMethodNotFoundException();
    }
  }
}
