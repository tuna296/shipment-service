package com.ncsgroup.shipment.server.exception.address;

import com.ncsgroup.shipment.server.exception.base.BaseException;

public class AddressNotFoundException extends BaseException {
  public AddressNotFoundException() {
    setCode("com.ncsgroup.shipment.server.exception.address.AddressNotFoundException");
  }

  public AddressNotFoundException(boolean isProvince, boolean isDistrict, boolean isWard) {
    if (isProvince) {
      setCode("com.ncsgroup.shipment.server.exception.address.AddressNotFoundException.Province");
      return;
    }
    if (isDistrict) {
      setCode("com.ncsgroup.shipment.server.exception.address.AddressNotFoundException.District");
      return;
    }
    if (isWard) {
      setCode("com.ncsgroup.shipment.server.exception.address.AddressNotFoundException.Ward");
      return;
    }
  }
}
