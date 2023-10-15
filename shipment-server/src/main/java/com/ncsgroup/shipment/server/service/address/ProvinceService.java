package com.ncsgroup.shipment.server.service.address;

import com.ncsgroup.shipment.server.dto.address.province.ProvincePageResponse;
import com.ncsgroup.shipment.server.dto.address.province.ProvinceResponse;
import com.ncsgroup.shipment.server.entity.address.Province;
import com.ncsgroup.shipment.server.service.base.BaseService;

public interface ProvinceService extends BaseService<Province> {
  ProvincePageResponse search(String keyword, int page, int size, boolean isAll);
  ProvinceResponse detail(String code);
}
