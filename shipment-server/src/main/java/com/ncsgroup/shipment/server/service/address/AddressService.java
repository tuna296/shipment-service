package com.ncsgroup.shipment.server.service.address;

import com.ncsgroup.shipment.server.dto.PageResponse;
import com.ncsgroup.shipment.server.dto.address.AddressResponse;
import com.ncsgroup.shipment.server.entity.address.Address;
import com.ncsgroup.shipment.server.service.base.BaseService;
import com.ncsgroup.shipment.client.dto.address.AddressRequest;

public interface AddressService extends BaseService<Address> {
  AddressResponse create(AddressRequest request);

  PageResponse<AddressResponse> list(String keyword, int size, int page, boolean isAll);

  AddressResponse detail(String id);

  AddressResponse update(AddressRequest request, String id);

  void delete(String id);
}
