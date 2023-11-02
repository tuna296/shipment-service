package com.ncsgroup.shipment.server.service.address;

import com.ncsgroup.shipment.server.configuration.ShipmentTestConfiguration;
import com.ncsgroup.shipment.server.dto.PageResponse;
import com.ncsgroup.shipment.server.dto.address.AddressResponse;
import com.ncsgroup.shipment.server.entity.address.Address;
import com.ncsgroup.shipment.server.exception.address.AddressNotFoundException;
import com.ncsgroup.shipment.server.repository.address.AddressRepository;
import dto.address.AddressRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebMvcTest(AddressServiceTest.class)
@ContextConfiguration(classes = ShipmentTestConfiguration.class)
public class AddressServiceTest {
  @Autowired
  private AddressService addressService;
  @MockBean
  private AddressRepository repository;
  private static final String mockId = "test";

  private AddressRequest mockRequest() {
    AddressRequest addressRequest = new AddressRequest();
    addressRequest.setProvinceCode("30");
    addressRequest.setDistrictCode("293");
    addressRequest.setWardCode("10081");
    addressRequest.setDetail("Tam Ky Kim Thanh Hai Duong");
    return addressRequest;
  }

  private Address mockEntity() {
    Address address = new Address();
    address.setProvinceCode("30");
    address.setDistrictCode("293");
    address.setWardCode("10081");
    address.setDetail("Tam Ky Kim Thanh Hai Duong");
    return address;
  }

  private AddressResponse mockResponse() {
    AddressResponse addressResponse = new AddressResponse();
    addressResponse.setId("idMock");
    addressResponse.setProvinces("30");
    addressResponse.setDistricts("293");
    addressResponse.setWards("10081");
    addressResponse.setDetail("Tam Ky Kim Thanh Hai Duong");
    return addressResponse;
  }

  @Test
  public void testCreate_WhenCreateSuccess_ReturnAddressResponse() throws Exception {
    AddressRequest mockRequest = mockRequest();
    Address mockEntity = mockEntity();
    Mockito.when(repository.save(mockEntity)).thenReturn(mockEntity);
    AddressResponse response = addressService.create(mockRequest);

    Assertions.assertThat(response.getId()).isEqualTo(mockEntity.getId());
    Assertions.assertThat(response.getProvinces()).isEqualTo(mockEntity.getProvinceCode());
    Assertions.assertThat(response.getDistricts()).isEqualTo(mockEntity.getDistrictCode());
    Assertions.assertThat(response.getWards()).isEqualTo(mockEntity.getWardCode());
    Assertions.assertThat(response.getDetail()).isEqualTo(mockEntity.getDetail());
  }

  @Test
  public void testList_WhenIsAll_ReturnResponseBody() throws Exception {
    AddressResponse addressResponse = mockResponse();
    Pageable pageable = PageRequest.of(0, 10);
    List<AddressResponse> list = new ArrayList<>();
    list.add(addressResponse);

    Page<AddressResponse> mockPage = new PageImpl<>(list);

    Mockito.when(repository.findAllAddress(pageable)).thenReturn(mockPage);

    PageResponse<AddressResponse> response = addressService.list(null, 10, 0, true);
    Assertions.assertThat(list.size()).isEqualTo(response.getAmount());
  }

  @Test
  public void testList_WhenSearchByKeyWord_ReturnResponseBody() throws Exception {
    AddressResponse addressResponse = mockResponse();
    Pageable pageable = PageRequest.of(0, 10);
    List<AddressResponse> list = new ArrayList<>();
    list.add(addressResponse);

    Page<AddressResponse> mockPage = new PageImpl<>(list);

    Mockito.when(repository.search(pageable, "Tam Ky")).thenReturn(mockPage);

    PageResponse<AddressResponse> response = addressService.list("Tam Ky", 10, 0, false);
    Assertions.assertThat(list.size()).isEqualTo(response.getAmount());
  }

}
