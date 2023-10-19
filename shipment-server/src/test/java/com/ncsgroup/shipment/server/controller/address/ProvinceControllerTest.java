package com.ncsgroup.shipment.server.controller.address;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ncsgroup.shipment.server.dto.address.province.ProvinceInfoResponse;
import com.ncsgroup.shipment.server.dto.address.province.ProvincePageResponse;
import com.ncsgroup.shipment.server.dto.address.province.ProvinceResponse;
import com.ncsgroup.shipment.server.entity.address.Province;
import com.ncsgroup.shipment.server.exception.address.AddressNotFoundException;
import com.ncsgroup.shipment.server.service.MessageService;
import com.ncsgroup.shipment.server.service.address.ProvinceService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static com.ncsgroup.shipment.server.constanst.Constants.MessageCode.DETAIL_PROVINCE;
import static com.ncsgroup.shipment.server.constanst.Constants.MessageCode.GET_PROVINCE_SUCCESS;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProvinceController.class)
public class ProvinceControllerTest {
  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private ProvinceService provinceService;
  @MockBean
  private MessageService messageService;
  @Autowired
  private ProvinceController provinceController;
  @Autowired
  ObjectMapper objectMapper;

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
    mockEntity.setNameEn("Hai Duong");
    mockEntity.setFullName("Tinh Hai Duong");
    mockEntity.setFullNameEn("Hai Duong Province");
    mockEntity.setCodeName("hai_duong");
    return mockEntity;
  }

  private ProvinceInfoResponse mockResponse() {
    ProvinceInfoResponse mockResponse = new ProvinceInfoResponse();
    mockResponse.setProvinceName("Ha Noi");
    mockResponse.setProvinceNameEn("Ha Noi");
    mockResponse.setProvinceCodeName("ha_noi");
    mockResponse.setCode("01");
    return mockResponse;
  }

  @Test
  void testList_WhenSearchByKeyWordAllFalse_Return200Body() throws Exception {
    ProvincePageResponse mock = new ProvincePageResponse();
    List<ProvinceResponse> list = new ArrayList<>();
    list.add(ProvinceResponse.from(
          mockProvince01().getCode(),
          mockProvince01().getName(),
          mockProvince01().getNameEn(),
          mockProvince01().getFullName(),
          mockProvince01().getFullNameEn(),
          mockProvince01().getCodeName()
    ));
    list.add(ProvinceResponse.from(
          mockProvince02().getCode(),
          mockProvince02().getName(),
          mockProvince02().getNameEn(),
          mockProvince02().getFullName(),
          mockProvince02().getFullNameEn(),
          mockProvince02().getCodeName()
    ));
    mock.setProvinceResponses(list);
    Mockito.when(messageService.getMessage(GET_PROVINCE_SUCCESS, "en")).thenReturn("success");
    Mockito.when(provinceService.list("", 10, 0, true)).thenReturn(mock);
    MvcResult mvcResult = mockMvc.perform(get("/api/v1/provinces")
                .param("keyword", "01")
                .param("size", String.valueOf(10))
                .param("page", String.valueOf(0))
                .param("all", String.valueOf(false)))
          .andExpect(status().isOk())
          .andDo(print())
          .andReturn();
    String responseBody = mvcResult.getResponse().getContentAsString();
    Assertions.assertEquals(responseBody,
          objectMapper.writeValueAsString(provinceController.list("01", 10, 0, false, "en")));
  }

  @Test
  void testList_WhenListIsAll_Returns200AndBody() throws Exception {
    ProvincePageResponse mock = new ProvincePageResponse();
    List<ProvinceResponse> list = new ArrayList<>();
    list.add(ProvinceResponse.from(
          mockProvince01().getCode(),
          mockProvince01().getName(),
          mockProvince01().getNameEn(),
          mockProvince01().getFullName(),
          mockProvince01().getFullNameEn(),
          mockProvince01().getCodeName()
    ));
    mock.setProvinceResponses(list);
    Mockito.when(messageService.getMessage(GET_PROVINCE_SUCCESS, "en")).thenReturn("success");
    Mockito.when(provinceService.list("", 10, 0, true)).thenReturn(mock);
    MvcResult mvcResult = mockMvc.perform(get("/api/v1/provinces")
                .param("keyword", "")
                .param("size", String.valueOf(10))
                .param("page", String.valueOf(0))
                .param("all", String.valueOf(true)))
          .andExpect(status().isOk())
          .andReturn();
    String responseBody = mvcResult.getResponse().getContentAsString();
    Assertions.assertEquals(responseBody,
          objectMapper.writeValueAsString(provinceController.list("", 10, 0, true, "en")));
  }

  @Test
  void testDetails_WhenCodeNotFound_ReturnProvinceNotFoundException() throws Exception {
    Mockito.when(provinceService.detail("ok")).thenThrow(new AddressNotFoundException(true, false, false));
    ResultActions resultActions = mockMvc.perform(
                get("/api/v1/provinces/details/{code}", "ok")
                      .contentType("application/json"))
          .andExpect(status().isNotFound())
          .andExpect(jsonPath("$.data.code")
                .value("com.ncsgroup.shipment.server.exception.address.AddressNotFoundException.Province"));
  }

  @Test
  void testDetails_WhenCodeExists_ReturnProvinceDetails() throws Exception {
    ProvinceInfoResponse mockResponse = mockResponse();
    Mockito.when(provinceService.detail("01")).thenReturn(mockResponse);
    Mockito.when(messageService.getMessage(DETAIL_PROVINCE, "en")).thenReturn("Get detail success");
    MvcResult mvcResult = mockMvc.perform(
                get("/api/v1/provinces/details/{code}", "01")
                      .contentType("application/json"))
          .andDo(print())
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.message").value("Get detail success"))
          .andReturn();
    String responseBody = mvcResult.getResponse().getContentAsString();
    Assertions.assertEquals(responseBody,
          objectMapper.writeValueAsString(provinceController.detail("01", "en")));
  }

}
