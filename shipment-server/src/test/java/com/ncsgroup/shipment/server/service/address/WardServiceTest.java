package com.ncsgroup.shipment.server.service.address;

import com.ncsgroup.shipment.server.configuration.ShipmentTestConfiguration;
import com.ncsgroup.shipment.server.dto.address.ward.WardInfoResponse;
import com.ncsgroup.shipment.server.dto.address.ward.WardPageResponse;
import com.ncsgroup.shipment.server.dto.address.ward.WardResponse;
import com.ncsgroup.shipment.server.entity.address.Ward;
import com.ncsgroup.shipment.server.exception.address.AddressNotFoundException;
import com.ncsgroup.shipment.server.repository.address.WardRepository;
import dto.address.SearchWardRequest;
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

@WebMvcTest(WardService.class)
@ContextConfiguration(classes = ShipmentTestConfiguration.class)
public class WardServiceTest {
  @MockBean
  private WardRepository repository;
  @Autowired
  private WardService wardService;

  private Ward mockWard() {
    Ward mockEntity = new Ward();
    mockEntity.setCode("10801");
    mockEntity.setName("Tam Ky");
    mockEntity.setNameEn("Tam Ky");
    mockEntity.setFullName("xa Tam Ky");
    mockEntity.setFullNameEn("Tam Ky ward");
    mockEntity.setCodeName("tam_ky");
    mockEntity.setDistrictCode("293");
    return mockEntity;
  }

  private SearchWardRequest mockSearchRequest(String keyword, String districtCode) {
    return new SearchWardRequest(keyword, districtCode);
  }

  private WardResponse mockWardResponse(Ward ward) {
    return new WardResponse(
          ward.getCode(),
          ward.getName(),
          ward.getNameEn(),
          ward.getFullName(),
          ward.getFullNameEn(),
          ward.getCodeName()
    );
  }

  private WardInfoResponse mockWardInfo(Ward ward) {
    return new WardInfoResponse(
          ward.getName(),
          ward.getNameEn(),
          ward.getCodeName(),
          ward.getCode());
  }

  @Test
  void testList_WhenAllFalseAndExistDistrictCode_ReturnDistrictPageResponse() {
    SearchWardRequest mockSearch = mockSearchRequest(null, "293");
    Ward mockEntity = mockWard();
    WardResponse mockResponse = mockWardResponse(mockEntity);
    List<WardResponse> list = new ArrayList<>();
    list.add(mockResponse);

    Mockito.when(repository.list("293")).thenReturn(list);
    Mockito.when(repository.count("293")).thenReturn(list.size());

    WardPageResponse response = wardService.search(mockSearch, 10, 0, true);
    Assertions.assertThat(list.size()).isEqualTo(response.getCount());
  }

  @Test
  void testList_WhenAllTrue_ReturnWardPageResponse() {
    Ward mockEntity = mockWard();
    WardResponse mockResponse = mockWardResponse(mockEntity);
    List<WardResponse> list = new ArrayList<>();
    list.add(mockResponse);

    Mockito.when(repository.list(null)).thenReturn(list);
    Mockito.when(repository.count((String) null)).thenReturn(list.size());

    WardPageResponse response = wardService.search(null, 10, 0, true);
    Assertions.assertThat(list.size()).isEqualTo(response.getCount());
  }

  @Test
  void testList_WhenAllFalse_ReturnWardPageResponse() {
    Pageable pageable = PageRequest.of(0, 5);
    Ward mockEntity = mockWard();
    SearchWardRequest mockSearch = mockSearchRequest("tam_ky", "293");
    WardResponse mockResponse = mockWardResponse(mockEntity);
    List<WardResponse> list = new ArrayList<>();
    list.add(mockResponse);

    Mockito.when(repository.countSearch("tam_ky", "293")).thenReturn(list.size());
    Mockito.when(repository.search("tam_ky", "293", pageable)).thenReturn(list);

    WardPageResponse response = wardService.search(mockSearch, 5, 0, false);
    Assertions.assertThat(list.size()).isEqualTo(response.getCount());
    Assertions.assertThat(list.size()).isEqualTo(response.getWardResponses().size());
  }

  @Test
  void testDetail_WhenCodeNotFound_ReturnsWardNotFoundException() {
    Mockito.when(repository.existsByCode("123")).thenReturn(false);
    Assertions.assertThatThrownBy(() -> wardService.detail("123")).isInstanceOf(AddressNotFoundException.class);
  }

  @Test
  void testDetail_WhenGetSuccess_ReturnResponseBody() {
    Ward mockEntity = mockWard();
    WardInfoResponse wardInfo = mockWardInfo(mockEntity);
    Mockito.when(repository.existsByCode("10081")).thenReturn(true);
    Mockito.when(repository.getByCode("10081")).thenReturn(wardInfo);
    WardInfoResponse response = wardService.detail("10081");
    Assertions.assertThat(mockEntity.getName()).isEqualTo(response.getWardName());
    Assertions.assertThat(mockEntity.getNameEn()).isEqualTo(response.getWardNameEn());
    Assertions.assertThat(mockEntity.getCodeName()).isEqualTo(response.getWardCodeName());
    Assertions.assertThat(mockEntity.getCode()).isEqualTo(response.getCode());
  }

}
