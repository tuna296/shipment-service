package com.ncsgroup.shipment.server.service.address;

import com.ncsgroup.shipment.server.configuration.ShipmentTestConfiguration;
import com.ncsgroup.shipment.server.dto.address.district.DistrictInfoResponse;
import com.ncsgroup.shipment.server.dto.address.district.DistrictPageResponse;
import com.ncsgroup.shipment.server.dto.address.district.DistrictResponse;
import com.ncsgroup.shipment.server.entity.address.District;
import com.ncsgroup.shipment.server.exception.address.AddressNotFoundException;
import com.ncsgroup.shipment.server.repository.address.DistrictRepository;
import com.ncsgroup.shipment.client.dto.address.SearchDistrictRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;

@WebMvcTest(DistrictService.class)
@ContextConfiguration(classes = ShipmentTestConfiguration.class)
public class DistrictServiceTest {
  @MockBean
  private DistrictRepository repository;
  @Autowired
  private DistrictService districtService;

  private District mockDistrict() {
    District mockEntity = new District();
    mockEntity.setCode("293");
    mockEntity.setName("Kim Thanh");
    mockEntity.setNameEn("Kim Thanh");
    mockEntity.setFullName("Huyen Kim Thanh");
    mockEntity.setFullNameEn("Kim Thanh District");
    mockEntity.setCodeName("kim_thanh");
    mockEntity.setProvinceCode("30");
    return mockEntity;
  }

  private District mockDistrict1() {
    District mockEntity = new District();
    mockEntity.setCode("294");
    mockEntity.setName("Kinh Mon");
    mockEntity.setNameEn("Kinh Mon");
    mockEntity.setFullName("Thi xa Kinh Mon");
    mockEntity.setFullNameEn("Kinh Mon tow");
    mockEntity.setCodeName("kinh_mon");
    mockEntity.setProvinceCode("31");
    return mockEntity;
  }

  private SearchDistrictRequest mockSearchRequest(String keyword, String provinceCode) {
    return new SearchDistrictRequest(keyword, provinceCode);
  }

  private DistrictResponse mockDistrictResponse(District district) {
    return new DistrictResponse(
          district.getCode(),
          district.getName(),
          district.getNameEn(),
          district.getFullName(),
          district.getFullNameEn(),
          district.getCodeName()
    );
  }

  private DistrictInfoResponse mockDistrictInfo(District district) {
    return new DistrictInfoResponse(
          district.getName(),
          district.getNameEn(),
          district.getCodeName(),
          district.getCode());
  }

  @Test
  void testList_WhenAllFalseAndExistProvinceCode_ReturnDistrictPageResponse() {
    SearchDistrictRequest mockSearch = mockSearchRequest(null, "30");
    District mockEntity = mockDistrict();
    District mockEntity1 = mockDistrict1();
    DistrictResponse mockResponse = mockDistrictResponse(mockEntity);
    DistrictResponse mockResponse1 = mockDistrictResponse(mockEntity1);
    List<DistrictResponse> list = new ArrayList<>();
    list.add(mockResponse);
    list.add(mockResponse1);

    Mockito.when(repository.list("30")).thenReturn(list);
    Mockito.when(repository.count("30")).thenReturn(list.size());

    DistrictPageResponse response = districtService.search(mockSearch, 10, 0, true);
    Assertions.assertThat(list.size()).isEqualTo(response.getCount());
  }

  @Test
  void testList_WhenAllTrue_ReturnDistrictPageResponse() {
    District mockEntity = mockDistrict();
    District mockEntity1 = mockDistrict1();
    DistrictResponse mockResponse = mockDistrictResponse(mockEntity);
    DistrictResponse mockResponse1 = mockDistrictResponse(mockEntity1);
    List<DistrictResponse> list = new ArrayList<>();
    list.add(mockResponse);
    list.add(mockResponse1);

    Mockito.when(repository.list(null)).thenReturn(list);
    Mockito.when(repository.count((String) null)).thenReturn(list.size());

    DistrictPageResponse response = districtService.search(null, 10, 0, true);
    Assertions.assertThat(list.size()).isEqualTo(response.getCount());
  }

  @Test
  void testList_WhenAllFalse_ReturnDistrictPageResponse() {
    Pageable pageable = PageRequest.of(0, 5);
    District mockEntity = mockDistrict();
    District mockEntity1 = mockDistrict1();
    SearchDistrictRequest mockSearch = mockSearchRequest("kim_thanh", "30");
    DistrictResponse mockResponse = mockDistrictResponse(mockEntity);
    DistrictResponse mockResponse1 = mockDistrictResponse(mockEntity1);
    List<DistrictResponse> list = new ArrayList<>();
    list.add(mockResponse);
    list.add(mockResponse1);

    Mockito.when(repository.countSearch("kim_thanh", "30")).thenReturn(list.size());
    Mockito.when(repository.search("kim_thanh", "30", pageable)).thenReturn(list);

    DistrictPageResponse response = districtService.search(mockSearch, 5, 0, false);
    Assertions.assertThat(list.size()).isEqualTo(response.getCount());
    Assertions.assertThat(list.size()).isEqualTo(response.getDistrictsResponse().size());
  }

  @Test
  void testDetail_WhenCodeNotFound_ReturnsDistrictNotFoundException() {
    Mockito.when(repository.existsByCode("123")).thenReturn(false);
    Assertions.assertThatThrownBy(() -> districtService.detail("123")).isInstanceOf(AddressNotFoundException.class);
  }

  @Test
  void testDetail_WhenGetSuccess_ReturnResponseBody() {
    District mockEntity = mockDistrict();
    DistrictInfoResponse districtInfo = mockDistrictInfo(mockEntity);
    Mockito.when(repository.existsByCode("01")).thenReturn(true);
    Mockito.when(repository.getByCode("01")).thenReturn(districtInfo);
    DistrictInfoResponse response = districtService.detail("01");
    Assertions.assertThat(mockEntity.getName()).isEqualTo(response.getDistrictName());
    Assertions.assertThat(mockEntity.getNameEn()).isEqualTo(response.getDistrictNameEn());
    Assertions.assertThat(mockEntity.getCodeName()).isEqualTo(response.getDistrictCodeName());
    Assertions.assertThat(mockEntity.getCode()).isEqualTo(response.getCode());
  }
}
