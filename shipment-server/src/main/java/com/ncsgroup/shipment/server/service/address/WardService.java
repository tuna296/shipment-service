package com.ncsgroup.shipment.server.service.address;

import com.ncsgroup.shipment.server.dto.address.ward.WardInfoResponse;
import com.ncsgroup.shipment.server.dto.address.ward.WardPageResponse;
import com.ncsgroup.shipment.client.dto.address.SearchWardRequest;

public interface WardService {
  WardPageResponse search(SearchWardRequest request, int size, int page, boolean isAll);

  WardInfoResponse detail(String code);

  void checkWardExist(String code);
}
