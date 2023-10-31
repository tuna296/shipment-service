package com.ncsgroup.shipment.server.service.address.impl;

import com.ncsgroup.shipment.server.dto.address.AddressResponse;
import com.ncsgroup.shipment.server.entity.address.Address;
import com.ncsgroup.shipment.server.exception.address.AddressNotFoundException;
import com.ncsgroup.shipment.server.repository.address.AddressRepository;
import com.ncsgroup.shipment.server.service.address.AddressService;
import com.ncsgroup.shipment.server.service.base.BaseServiceImpl;
import dto.address.AddressRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j

public class AddressServiceImpl extends BaseServiceImpl<Address> implements AddressService {
  private final AddressRepository repository;

  public AddressServiceImpl(AddressRepository repository) {
    super(repository);
    this.repository = repository;
  }

  @Override
  @Transactional
  public AddressResponse create(AddressRequest request) {
    log.info("(create) request: {}", request);
    Address address = new Address();
    convertToEntity(request, address);
    AddressResponse response = new AddressResponse();
    this.convertToResponse(create(address), response);
    return response;
  }

  @Override
  public AddressResponse detail(String id) {
    log.info("(detail) address: {}", id);
    Address address = this.find(id);
    AddressResponse response = new AddressResponse();
    this.convertToResponse(address, response);
    return response;
  }

  private void convertToEntity(AddressRequest request, Address address) {
    address.setProvinceCode(request.getProvinceCode());
    address.setDistrictCode(request.getDistrictCode());
    address.setWardCode(request.getWardCode());
    address.setDetail(request.getDetail());
  }

  private void convertToResponse(Address address, AddressResponse response) {
    response.setProvinces(address.getProvinceCode());
    response.setDistricts(address.getDistrictCode());
    response.setWards(address.getWardCode());
    response.setDetail(address.getDetail());
    response.setId(address.getId());
  }

  private Address find(String id) {
    log.info("(find) by id: {}", id);
    return repository.findById(id).orElseThrow(AddressNotFoundException::new);
  }
}
