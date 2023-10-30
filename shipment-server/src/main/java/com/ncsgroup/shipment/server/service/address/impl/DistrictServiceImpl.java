package com.ncsgroup.shipment.server.service.address.impl;

import com.ncsgroup.shipment.server.dto.address.district.DistrictInfoResponse;
import com.ncsgroup.shipment.server.dto.address.district.DistrictPageResponse;
import com.ncsgroup.shipment.server.dto.address.district.DistrictResponse;
import com.ncsgroup.shipment.server.entity.address.District;
import com.ncsgroup.shipment.server.exception.address.AddressNotFoundException;
import com.ncsgroup.shipment.server.repository.address.DistrictRepository;
import com.ncsgroup.shipment.server.service.address.DistrictService;
import com.ncsgroup.shipment.server.service.base.BaseServiceImpl;
import dto.address.SearchDistrictRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;

import java.util.List;

@Slf4j
public class DistrictServiceImpl extends BaseServiceImpl<District> implements DistrictService {
  private final DistrictRepository repository;

  public DistrictServiceImpl(DistrictRepository repository) {
    super(repository);
    this.repository = repository;
  }

  @Override
  public DistrictPageResponse search(SearchDistrictRequest request, int size, int page, boolean isAll) {
    log.info("(search) request: {}, size:{}, page:{}, isAll: {}", request, size, page, isAll);
    String keyword = (request == null || request.getKeyword() == null) ? null : request.getKeyword().toLowerCase();
    String provinceCode = (request == null) ? null : request.getProvinceCode();
    List<DistrictResponse> districts = isAll ? repository.list(provinceCode) :
          repository.search(keyword, provinceCode, PageRequest.of(page, size));
    int count = isAll ? repository.count(provinceCode) : repository.countSearch(keyword, provinceCode);

    return DistrictPageResponse.of(districts, count);
  }

  @Override
  public DistrictInfoResponse detail(String code) {
    log.info("(detail)code: {}", code);
    this.checkDistrictExist(code);
    return repository.getByCode(code);
  }

  @Override
  public void checkDistrictExist(String code) {
    log.debug("(checkDistrictExist)");

    if (!repository.existsByCode(code)) {
      log.error("(checkDistrictExist) ============= AddressExistException");
      throw new AddressNotFoundException(false, true, false);
    }
  }

}
