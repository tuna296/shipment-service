package com.ncsgroup.shipment.server.service.address.impl;

import com.ncsgroup.shipment.server.dto.address.province.ProvincePageResponse;
import com.ncsgroup.shipment.server.dto.address.province.ProvinceResponse;
import com.ncsgroup.shipment.server.entity.address.Province;
import com.ncsgroup.shipment.server.exception.address.AddressNotFoundException;
import com.ncsgroup.shipment.server.repository.address.ProvinceRepository;
import com.ncsgroup.shipment.server.service.address.ProvinceService;
import com.ncsgroup.shipment.server.service.base.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ProvinceServiceImpl extends BaseServiceImpl<Province> implements ProvinceService {
  private final ProvinceRepository repository;

  public ProvinceServiceImpl(ProvinceRepository repository) {
    super(repository);
    this.repository = repository;
  }

  @Override
  public ProvincePageResponse search(String keyword, int page, int size, boolean isAll) {
    log.info("(search) keyword: {}, page:{}, size:{}, isAll: {}", keyword, page, size, isAll);
    List<ProvinceResponse> list = new ArrayList<>();
    Pageable pageable = PageRequest.of(page, size);
    List<Province> provinces = isAll ?
          repository.findAll() : repository.search(keyword,pageable);
    for (Province province : provinces) {
      list.add(convertToResponse(province));
    }
    return new ProvincePageResponse(list, isAll ? provinces.size() : repository.countSearch(keyword));

}

  @Override
  public ProvinceResponse detail(String code) {
    log.info("detail by code {}", code);
    this.checkProvinceExist(code);
    return repository.getByCode(code);
  }

  public void checkProvinceExist(String code) {
    log.debug("check province by code {}", code);
    if (!repository.existsByCode(code)) {
      log.error("(checkProvinceExist) ========> (AddressNotFoundException)");
      throw new AddressNotFoundException(true, false, false);
    }
  }

  public ProvinceResponse convertToResponse(Province province) {
    return ProvinceResponse.from(
          province.getName(),
          province.getCode(),
          province.getCodeName(),
          province.getNameEn(),
          province.getFullName(),
          province.getFullNameEn()
    );
  }

}
