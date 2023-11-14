package com.ncsgroup.shipment.server.facade;

import com.ncsgroup.shipment.server.dto.address.AddressResponse;
import dto.address.AddressRequest;

public interface AddressFacadeService {
  AddressResponse createAddress(AddressRequest request);
  AddressResponse updateAddress(AddressRequest request, String id);

}
