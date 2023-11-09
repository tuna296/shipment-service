package com.ncsgroup.shipment.server.facade.impl;

import com.ncsgroup.shipment.server.dto.address.AddressResponse;
import com.ncsgroup.shipment.server.facade.AddressFacadeService;
import com.ncsgroup.shipment.server.service.address.AddressService;
import com.ncsgroup.shipment.server.service.address.DistrictService;
import com.ncsgroup.shipment.server.service.address.ProvinceService;
import com.ncsgroup.shipment.server.service.address.WardService;
import dto.address.AddressRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
public class AddressFacadeServiceImpl implements AddressFacadeService {
  private final AddressService addressService;
  private final ProvinceService provinceService;
  private final DistrictService districtService;
  private final WardService wardService;

  @Override
  @Transactional
  public AddressResponse createAddress(AddressRequest request) {
    log.info("(createAddress) request: {}", request);

    this.checkAddressComponentsExist(request);

    return addressService.create(request);
  }

  private void checkAddressComponentsExist(AddressRequest request) {
    log.debug("checkAddressComponentsExist");

    if (Objects.nonNull(request.getProvinceCode()))
      provinceService.checkProvinceExist(request.getProvinceCode());

    if (Objects.nonNull(request.getDistrictCode()))
      districtService.checkDistrictExist(request.getDistrictCode());

    if (Objects.nonNull(request.getWardCode()))
      wardService.checkWardExist(request.getWardCode());
  }


}
