package com.ncsgroup.shipment.server.facade;

import com.ncsgroup.shipment.server.configuration.ShipmentTestConfiguration;
import com.ncsgroup.shipment.server.dto.address.AddressResponse;
import com.ncsgroup.shipment.server.dto.address.district.DistrictInfoResponse;
import com.ncsgroup.shipment.server.dto.address.province.ProvinceInfoResponse;
import com.ncsgroup.shipment.server.dto.address.ward.WardInfoResponse;
import com.ncsgroup.shipment.server.exception.address.AddressNotFoundException;
import com.ncsgroup.shipment.server.service.address.AddressService;
import com.ncsgroup.shipment.server.service.address.DistrictService;
import com.ncsgroup.shipment.server.service.address.ProvinceService;
import com.ncsgroup.shipment.server.service.address.WardService;
import dto.address.AddressRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

@WebMvcTest(AddressFacadeService.class)
@ContextConfiguration(classes = ShipmentTestConfiguration.class)
public class AddressFacadeTest {
  @MockBean
  private AddressService addressService;
  @MockBean
  private ProvinceService provinceService;
  @MockBean
  private DistrictService districtService;
  @MockBean
  private WardService wardService;
  @Autowired
  private AddressFacadeService addressFacadeService;

  private AddressRequest mockAddressRequest() {
    AddressRequest addressRequest = new AddressRequest();
    addressRequest.setProvinceCode("30");
    addressRequest.setDistrictCode("293");
    addressRequest.setWardCode("10081");
    addressRequest.setDetail("Tam Ky Kim Thanh Hai Duong");
    return addressRequest;
  }

  private AddressResponse mockServiceResponse() {
    AddressResponse addressResponse = new AddressResponse();
    addressResponse.setProvinces("30");
    addressResponse.setDistricts("293");
    addressResponse.setWards("10081");
    addressResponse.setDetail("Tam Ky Kim Thanh Hai Duong");
    return addressResponse;
  }


  private AddressResponse mockFacadeResponse() {
    AddressResponse addressResponse = new AddressResponse();
    addressResponse.setId("idMock");
    addressResponse.setProvinces("Hai Duong");
    addressResponse.setDistricts("Kim Thanh");
    addressResponse.setWards("Tam Ky");
    addressResponse.setDetail("Tam Ky Kim Thanh Hai Duong");
    return addressResponse;
  }

  private ProvinceInfoResponse mockProvinceInfo() {
    ProvinceInfoResponse provinceInfoResponse = new ProvinceInfoResponse();
    provinceInfoResponse.setProvinceName("Hai Duong");
    provinceInfoResponse.setProvinceNameEn("Hai Duong");
    provinceInfoResponse.setProvinceCodeName("hai_duong");
    provinceInfoResponse.setCode("30");
    return provinceInfoResponse;
  }

  private DistrictInfoResponse mockDistrictInfo() {
    DistrictInfoResponse districtInfoResponse = new DistrictInfoResponse();
    districtInfoResponse.setDistrictName("Kim Thanh");
    districtInfoResponse.setDistrictNameEn("Kim Thanh");
    districtInfoResponse.setDistrictCodeName("kim_thanh");
    districtInfoResponse.setCode("293");
    return districtInfoResponse;
  }

  private WardInfoResponse mockWardInfo() {
    WardInfoResponse wardInfoResponse = new WardInfoResponse();
    wardInfoResponse.setWardName("Tam Ky");
    wardInfoResponse.setWardNameEn("Tam Ky");
    wardInfoResponse.setWardCodeName("tam_ky");
    wardInfoResponse.setCode("10081");
    return wardInfoResponse;
  }


  @Test
  void testCreateAddress_WhenProvinceCodeNull_Return_AddressNotFound() throws Exception {
    AddressRequest request = mockAddressRequest();
    Mockito.doThrow(new AddressNotFoundException(true, false, false)).when(provinceService).checkProvinceExist(request.getProvinceCode());

    Assertions.assertThatThrownBy(() -> addressFacadeService.createAddress(request))
          .isInstanceOf(AddressNotFoundException.class)
          .hasFieldOrPropertyWithValue("code", "com.ncsgroup.shipment.server.exception.address.AddressNotFoundException.Province");
  }

  @Test
  void testCreateAddress_WhenDistrictCodeNull_Return_AddressNotFound() throws Exception {
    AddressRequest request = mockAddressRequest();
    Mockito.doThrow(new AddressNotFoundException(false, true, false)).when(wardService).checkWardExist(request.getWardCode());

    Assertions.assertThatThrownBy(() -> addressFacadeService.createAddress(request))
          .isInstanceOf(AddressNotFoundException.class)
          .hasFieldOrPropertyWithValue("code", "com.ncsgroup.shipment.server.exception.address.AddressNotFoundException.District");
  }

  @Test
  void testCreateAddress_WhenWardCodeNull_Return_AddressNotFound() throws Exception {
    AddressRequest request = mockAddressRequest();
    Mockito.doThrow(new AddressNotFoundException(false, false, true)).when(wardService).checkWardExist(request.getWardCode());

    Assertions.assertThatThrownBy(() -> addressFacadeService.createAddress(request))
          .isInstanceOf(AddressNotFoundException.class)
          .hasFieldOrPropertyWithValue("code", "com.ncsgroup.shipment.server.exception.address.AddressNotFoundException.Ward");
  }

  @Test
  void test_CreateAddress_WhenSuccess_Return_Response() {
    AddressRequest mockRequest = mockAddressRequest();
    AddressResponse mockFacadeResponse = mockFacadeResponse();

    Mockito.when(addressService.create(mockRequest)).thenReturn(mockFacadeResponse);

    AddressResponse response = addressFacadeService.createAddress(mockRequest);
    Assertions.assertThat(response.getProvinces()).isEqualTo(mockFacadeResponse.getProvinces());
    Assertions.assertThat(response.getDistricts()).isEqualTo(mockFacadeResponse.getDistricts());
    Assertions.assertThat(response.getWards()).isEqualTo(mockFacadeResponse.getWards());
    Assertions.assertThat(response.getDetail()).isEqualTo(mockFacadeResponse.getDetail());
  }

}
