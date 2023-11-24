package com.ncsgroup.shipment.server.controller.address;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ncsgroup.shipment.server.dto.address.district.DistrictInfoResponse;
import com.ncsgroup.shipment.server.dto.address.district.DistrictPageResponse;
import com.ncsgroup.shipment.server.dto.address.district.DistrictResponse;
import com.ncsgroup.shipment.server.entity.address.District;
import com.ncsgroup.shipment.server.exception.address.AddressNotFoundException;
import com.ncsgroup.shipment.server.service.MessageService;
import com.ncsgroup.shipment.server.service.address.DistrictService;
import dto.address.SearchDistrictRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static com.ncsgroup.shipment.server.constanst.Constants.MessageCode.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DistrictController.class)
public class DistrictControllerTest {
  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private DistrictService districtService;
  @MockBean
  private MessageService messageService;
  @Autowired
  private DistrictController districtController;
  @Autowired
  ObjectMapper objectMapper;

  private District mockDistrict() {
    District mockEntity = new District();
    mockEntity.setCode("294");
    mockEntity.setName("Kinh Mon");
    mockEntity.setNameEn("Kinh Mon");
    mockEntity.setFullName("Thi xa Kinh Mon");
    mockEntity.setFullNameEn("Kinh Mon tow");
    mockEntity.setCodeName("kinh_mon");
    mockEntity.setProvinceCode("30");
    return mockEntity;
  }

  private District mockDistrict1() {
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

  private DistrictInfoResponse mockDistrictInfo(District district) {
    return new DistrictInfoResponse(
          district.getName(),
          district.getNameEn(),
          district.getCodeName(),
          district.getCode());
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

  @Test
  void testList_WhenAllTrue_Return200Body() throws Exception {
    DistrictPageResponse mockPage = new DistrictPageResponse();
    District mockEntity = mockDistrict();
    District mockEntity1 = mockDistrict1();

    List<DistrictResponse> list = new ArrayList<>();
    list.add(mockDistrictResponse(mockEntity));
    list.add(mockDistrictResponse(mockEntity1));
    mockPage.setDistrictsResponse(list);

    Mockito.when(messageService.getMessage(GET_DISTRICT_SUCCESS, "en")).thenReturn("Success");
    Mockito.when(districtService.search(null, 10, 0, true)).thenReturn(mockPage);

    MvcResult mvcResult = mockMvc.perform(
                post("/api/v1/districts")
                      .param("size", String.valueOf(10))
                      .param("page", String.valueOf(0))
                      .param("all", String.valueOf(true)))
          .andExpect(status().isOk())
          .andDo(print())
          .andReturn();
    String responseBody = mvcResult.getResponse().getContentAsString();
    Assertions.assertEquals(responseBody,
          objectMapper.writeValueAsString(districtController.list(null, 10, 0, true, "en")));
  }

  @Test
  void testList_WhenAllFalse_Return200Body() throws Exception {
    DistrictPageResponse mockPage = new DistrictPageResponse();
    District mockEntity = mockDistrict();
    District mockEntity1 = mockDistrict1();
    List<DistrictResponse> list = new ArrayList<>();
    list.add(mockDistrictResponse(mockEntity));
    list.add(mockDistrictResponse(mockEntity1));
    mockPage.setDistrictsResponse(list);
    SearchDistrictRequest request = new SearchDistrictRequest("kim thanh", "30");
    Mockito.when(messageService.getMessage(GET_DISTRICT_SUCCESS, "en")).thenReturn("Success");
    Mockito.when(districtService.search(request, 10, 0, false)).thenReturn(mockPage);

    MvcResult mvcResult = mockMvc.perform(
                post("/api/v1/districts")
                      .contentType("application/json")
                      .content(objectMapper.writeValueAsString(request))
                      .param("size", String.valueOf(10))
                      .param("page", String.valueOf(0))
                      .param("all", "false"))
          .andExpect(status().isOk())
          .andDo(print())
          .andReturn();
    String responseBody = mvcResult.getResponse().getContentAsString();
    Assertions.assertEquals(responseBody,
          objectMapper.writeValueAsString(districtController.list(request, 10, 0, false, "en")));
  }

  @Test
  void testDetails_WhenCodeNotFound_ReturnDistrictNotFoundException() throws Exception {
    Mockito.when(districtService.detail("ok")).thenThrow(new AddressNotFoundException(false, true, false));
    mockMvc.perform(
                get("/api/v1/districts/details/{code}", "ok")
                      .contentType("application/json"))
          .andExpect(status().isNotFound())
          .andExpect(jsonPath("$.data.code")
                .value("com.ncsgroup.shipment.server.exception.address.AddressNotFoundException.District"));
  }

  @Test
  void testDetails_WhenCodeExists_ReturnDistrictDetails() throws Exception {
    District mockEntity = mockDistrict();
    mockEntity.setCode("test");
    DistrictInfoResponse mockResponse = mockDistrictInfo(mockEntity);
    Mockito.when(districtService.detail("test")).thenReturn(mockResponse);
    Mockito.when(messageService.getMessage(DETAIL_DISTRICT, "en")).thenReturn("Get detail success");
    MvcResult mvcResult = mockMvc.perform(
                get("/api/v1/districts/details/{code}", "test")
                      .contentType("application/json"))
          .andDo(print())
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.message").value("Get detail success"))
          .andReturn();
    String responseBody = mvcResult.getResponse().getContentAsString();
    Assertions.assertEquals(responseBody,
          objectMapper.writeValueAsString(districtController.detail("test", "en")));
  }
}
