package com.ncsgroup.shipment.server.service.address.impl;

import com.ncsgroup.shipment.server.dto.address.AddressPageResponse;
import com.ncsgroup.shipment.server.dto.address.AddressResponse;
import com.ncsgroup.shipment.server.entity.address.Address;
import com.ncsgroup.shipment.server.repository.address.AddressRepository;
import com.ncsgroup.shipment.server.repository.address.WardRepository;
import com.ncsgroup.shipment.server.service.address.AddressService;
import com.ncsgroup.shipment.server.service.base.BaseServiceImpl;
import dto.address.AddressRequest;

public class AddressServiceImpl extends BaseServiceImpl<Address> implements AddressService {
  private final AddressRepository repository;

  public AddressServiceImpl(AddressRepository repository) {
    super(repository);
    this.repository = repository;
  }

  @Override
  public AddressResponse create(AddressRequest request) {
    return null;
  }

  @Override
  public AddressResponse detail(String id) {
    return null;
  }

  @Override
  public AddressPageResponse list(String keyword, int size, int page, boolean isAll) {
    return null;
  }

}
