package com.ncsgroup.shipment.server.service.address;

import com.ncsgroup.shipment.server.dto.address.AddressPageResponse;
import com.ncsgroup.shipment.server.dto.address.AddressResponse;
import com.ncsgroup.shipment.server.entity.address.Address;
import com.ncsgroup.shipment.server.service.base.BaseService;
import dto.address.AddressRequest;

public interface AddressService extends BaseService<Address> {
  AddressResponse create(AddressRequest request);
  AddressResponse detail(String id);
  AddressPageResponse list(String keyword, int size, int page, boolean isAll);

}
