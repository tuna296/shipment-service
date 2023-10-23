package com.ncsgroup.shipment.server.service.address;

import com.ncsgroup.shipment.server.dto.address.district.DistrictInfoResponse;
import com.ncsgroup.shipment.server.dto.address.district.DistrictPageResponse;
import com.ncsgroup.shipment.server.entity.address.District;
import com.ncsgroup.shipment.server.service.base.BaseService;
import dto.address.SearchDistrictRequest;

public interface DistrictService extends BaseService<District> {
  DistrictPageResponse search(SearchDistrictRequest request, int size, int page, boolean isAll);

  DistrictInfoResponse detail(String code);
}
