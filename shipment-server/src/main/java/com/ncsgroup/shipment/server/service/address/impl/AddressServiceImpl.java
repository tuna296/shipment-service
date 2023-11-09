package com.ncsgroup.shipment.server.service.address.impl;

import com.ncsgroup.shipment.server.dto.PageResponse;
import com.ncsgroup.shipment.server.dto.address.AddressResponse;
import com.ncsgroup.shipment.server.entity.address.Address;
import com.ncsgroup.shipment.server.exception.address.AddressNotFoundException;
import com.ncsgroup.shipment.server.repository.address.AddressRepository;
import com.ncsgroup.shipment.server.service.address.AddressService;
import com.ncsgroup.shipment.server.service.base.BaseServiceImpl;
import dto.address.AddressRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

@Slf4j

public class AddressServiceImpl extends BaseServiceImpl<Address> implements AddressService {
  private final AddressRepository repository;

  public AddressServiceImpl(AddressRepository repository) {
    super(repository);
    this.repository = repository;
  }

  @Override
  @Transactional
  public AddressResponse create(AddressRequest request) {
    log.info("(create) request: {}", request);
    Address address = new Address(
          request.getWardCode(),
          request.getDistrictCode(),
          request.getProvinceCode(),
          request.getDetail()
    );
    repository.saveAndFlush(address);

    return repository.findAddressById(address.getId());
  }

  @Override
  public PageResponse<AddressResponse> list(String keyword, int size, int page, boolean isAll) {
    log.info("(list)name: {}, size : {}, page: {}, isAll: {}", keyword, size, page, isAll);

    Page<AddressResponse> pageResponse = isAll ?
          repository.findAllAddress(PageRequest.of(page, size)) : repository.search(PageRequest.of(page, size), keyword);

    return PageResponse.of(pageResponse.getContent(), (int) pageResponse.getTotalElements());

  }

  @Override
  public AddressResponse detail(String id) {
    log.info("(detail) address: {}", id);

    return checkAddressExist(id);
  }

  private AddressResponse checkAddressExist(String id) {
    log.debug("(checkAddressExist) by id: {}", id);
    if (repository.findAddressById(id) == null) {
      log.error("(checkAddressExist) ======> AddressNotFoundException");
      throw new AddressNotFoundException();
    } else
      return repository.findAddressById(id);
  }

}
