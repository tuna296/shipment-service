package com.ncsgroup.shipment.server.controller.address;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ncsgroup.shipment.server.dto.address.ward.WardInfoResponse;
import com.ncsgroup.shipment.server.dto.address.ward.WardPageResponse;
import com.ncsgroup.shipment.server.dto.address.ward.WardResponse;
import com.ncsgroup.shipment.server.entity.address.Ward;
import com.ncsgroup.shipment.server.exception.address.AddressNotFoundException;
import com.ncsgroup.shipment.server.service.MessageService;
import com.ncsgroup.shipment.server.service.address.WardService;
import com.ncsgroup.shipment.client.dto.address.SearchWardRequest;
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

@WebMvcTest(WardController.class)

public class WardControllerTest {
  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private WardService wardService;
  @MockBean
  private MessageService messageService;
  @Autowired
  private WardController wardController;
  @Autowired
  ObjectMapper objectMapper;

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

  private WardInfoResponse mockWardInfo(Ward ward) {
    return new WardInfoResponse(
          ward.getName(),
          ward.getNameEn(),
          ward.getCodeName(),
          ward.getCode());
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

  @Test
  void testList_WhenAllTrue_Return200Body() throws Exception {
    WardPageResponse mockPage = new WardPageResponse();
    Ward mockEntity = mockWard();

    List<WardResponse> list = new ArrayList<>();
    list.add(mockWardResponse(mockEntity));
    mockPage.setWardResponses(list);

    Mockito.when(messageService.getMessage(GET_WARD_SUCCESS, "en")).thenReturn("Success");
    Mockito.when(wardService.search(null, 10, 0, true)).thenReturn(mockPage);

    MvcResult mvcResult = mockMvc.perform(
                post("/api/v1/wards")
                      .param("size", String.valueOf(10))
                      .param("page", String.valueOf(0))
                      .param("all", String.valueOf(true)))
          .andExpect(status().isOk())
          .andDo(print())
          .andReturn();
    String responseBody = mvcResult.getResponse().getContentAsString();
    Assertions.assertEquals(responseBody,
          objectMapper.writeValueAsString(wardController.list(null, 10, 0, true, "en")));
  }

  @Test
  void testList_WhenAllAllSearchByRequest_Return200Body() throws Exception {
    WardPageResponse mockPage = new WardPageResponse();
    Ward mockEntity = mockWard();
    SearchWardRequest request = new SearchWardRequest("tam ky", "293");

    List<WardResponse> list = new ArrayList<>();
    list.add(mockWardResponse(mockEntity));

    mockPage.setWardResponses(list);

    Mockito.when(messageService.getMessage(GET_WARD_SUCCESS, "en")).thenReturn("Success");
    Mockito.when(wardService.search(request, 10, 0, false)).thenReturn(mockPage);

    MvcResult mvcResult = mockMvc.perform(
                post("/api/v1/wards")
                      .contentType("application/json")
                      .content(objectMapper.writeValueAsString(request))
                      .param("size", String.valueOf(10))
                      .param("page", String.valueOf(0))
                      .param("all", String.valueOf(false)))
          .andExpect(status().isOk())
          .andDo(print())
          .andReturn();
    String responseBody = mvcResult.getResponse().getContentAsString();
    Assertions.assertEquals(responseBody,
          objectMapper.writeValueAsString(wardController.list(request, 10, 0, false, "en")));
  }

  @Test
  void testDetails_WhenCodeNotFound_ReturnWardNotFoundException() throws Exception {
    Mockito.when(wardService.detail("04")).thenThrow(new AddressNotFoundException(false, false, true));
    mockMvc.perform(
                get("/api/v1/wards/details/{code}", "04")
                      .contentType("application/json"))
          .andExpect(status().isNotFound())
          .andExpect(jsonPath("$.data.code")
                .value("com.ncsgroup.shipment.server.exception.address.AddressNotFoundException.Ward"));
  }

  @Test
  void testDetails_WhenCodeExists_ReturnWardDetails() throws Exception {
    Ward mockEntity = mockWard();
    mockEntity.setCode("04");
    WardInfoResponse mockResponse = mockWardInfo(mockEntity);
    Mockito.when(wardService.detail("test")).thenReturn(mockResponse);
    Mockito.when(messageService.getMessage(DETAIL_WARD, "en")).thenReturn("Get success");
    MvcResult mvcResult = mockMvc.perform(
                get("/api/v1/wards/details/{code}", "04")
                      .contentType("application/json"))
          .andDo(print())
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.message").value("Get success"))
          .andReturn();
    String responseBody = mvcResult.getResponse().getContentAsString();
    Assertions.assertEquals(responseBody,
          objectMapper.writeValueAsString(wardController.detail("04", "en")));
  }
}
