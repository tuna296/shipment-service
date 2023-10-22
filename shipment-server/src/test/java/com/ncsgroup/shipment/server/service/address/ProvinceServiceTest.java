package com.ncsgroup.shipment.server.service.address;

import com.ncsgroup.shipment.server.configuration.ShipmentTestConfiguration;
import com.ncsgroup.shipment.server.dto.address.province.ProvinceInfoResponse;
import com.ncsgroup.shipment.server.dto.address.province.ProvincePageResponse;
import com.ncsgroup.shipment.server.entity.address.Province;
import com.ncsgroup.shipment.server.exception.address.AddressNotFoundException;
import com.ncsgroup.shipment.server.repository.address.ProvinceRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

@WebMvcTest(ProvinceService.class)
@ContextConfiguration(classes = ShipmentTestConfiguration.class)
public class ProvinceServiceTest {
  @MockBean
  private ProvinceRepository repository;
  @Autowired
  private ProvinceService provinceService;

  private Province mockProvince01() {
    Province mockEntity = new Province();
    mockEntity.setCode("01");
    mockEntity.setName("Ha Noi");
    mockEntity.setNameEn("Ha Noi");
    mockEntity.setFullName("Thanh Pho Ha Noi");
    mockEntity.setFullNameEn("Ha Noi City");
    mockEntity.setCodeName("ha_noi");
    return mockEntity;
  }

  private Province mockProvince02() {
    Province mockEntity = new Province();
    mockEntity.setCode("02");
    mockEntity.setName("Hai Duong");
    mockEntity.setNameEn("Ha Duong");
    mockEntity.setFullName("Tinh Hai Duong");
    mockEntity.setFullNameEn("Hai Duong Province");
    mockEntity.setCodeName("hai_duong");
    return mockEntity;
  }

  private ProvinceInfoResponse mockProvinceInfo(Province province) {
    return ProvinceInfoResponse.of(province.getName(), province.getNameEn(), province.getCodeName(), province.getCode());
  }

  @Test
  void testList_WhenAllTrue_ReturnProvincePageResponse() {
    Province mockProvince01 = mockProvince01();
    Province mockProvince02 = mockProvince02();
    List<Province> list = new ArrayList<>();
    list.add(mockProvince01);
    list.add(mockProvince02);
    Mockito.when(repository.findAll()).thenReturn(list);
    ProvincePageResponse response = provinceService.list(null, 5, 0, true);
    Assertions.assertThat(list.size()).isEqualTo(response.getCount());
  }

  @Test
  void testList_WhenAllFalse_ReturnProvincePageResponse() {
    Pageable pageable = PageRequest.of(0, 5);
    Province mockProvince01 = mockProvince01();
    Province mockProvince02 = mockProvince02();
    List<Province> list = new ArrayList<>();
    list.add(mockProvince01);
    list.add(mockProvince02);
    Mockito.when(repository.search("ha_noi", pageable)).thenReturn(list);
    Mockito.when(repository.countSearch("ha_noi")).thenReturn(list.size());
    ProvincePageResponse response = provinceService.list("ha_noi", 5, 0, false);
    Assertions.assertThat(list.size()).isEqualTo(response.getCount());
    Assertions.assertThat(list.size()).isEqualTo(response.getProvinceResponses().size());
  }

  @Test
  void testDetail_WhenCodeNotFound_ReturnsProvinceNotFoundException() {
    Mockito.when(repository.existsByCode("011")).thenReturn(false);
    Assertions.assertThatThrownBy(() -> provinceService.detail("011")).isInstanceOf(AddressNotFoundException.class);
  }

  @Test
  void testDetail_WhenGetSuccess_ReturnResponseBody() {
    Province mockEntity = mockProvince01();
    ProvinceInfoResponse provinceInfo = mockProvinceInfo(mockEntity);
    Mockito.when(repository.existsByCode("01")).thenReturn(true);
    Mockito.when(repository.getByCode("01")).thenReturn(provinceInfo);
    ProvinceInfoResponse response = provinceService.detail("01");
    Assertions.assertThat(mockEntity.getName()).isEqualTo(response.getProvinceName());
    Assertions.assertThat(mockEntity.getNameEn()).isEqualTo(response.getProvinceNameEn());
    Assertions.assertThat(mockEntity.getCodeName()).isEqualTo(response.getProvinceCodeName());
    Assertions.assertThat(mockEntity.getCode()).isEqualTo(response.getCode());
  }
}
