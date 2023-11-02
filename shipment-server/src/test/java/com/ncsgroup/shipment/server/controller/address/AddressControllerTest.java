package com.ncsgroup.shipment.server.controller.address;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ncsgroup.shipment.server.dto.PageResponse;
import com.ncsgroup.shipment.server.dto.address.AddressResponse;
import com.ncsgroup.shipment.server.entity.address.Address;
import com.ncsgroup.shipment.server.exception.address.AddressNotFoundException;
import com.ncsgroup.shipment.server.facade.AddressFacadeService;
import com.ncsgroup.shipment.server.service.MessageService;
import com.ncsgroup.shipment.server.service.address.AddressService;
import dto.address.AddressRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;;
import static com.ncsgroup.shipment.server.constanst.Constants.MessageCode.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AddressController.class)
public class AddressControllerTest {
  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private AddressService addressService;
  @MockBean
  private AddressFacadeService addressFacadeService;
  @MockBean
  private MessageService messageService;
  @Autowired
  ObjectMapper objectMapper;
  @Autowired
  private AddressController addressController;

  public String mockId = "test";

  private AddressRequest mockAddressRequest() {
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

  private AddressResponse mockAddressResponse() {
    AddressResponse response = new AddressResponse();
    response.setProvinces("30");
    response.setDistricts("293");
    response.setWards("10081");
    response.setDetail("Tam Ky Kim Thanh Hai Duong");
    return response;
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
  private AddressResponse addressResponse() {
    AddressResponse addressResponse = new AddressResponse();
    addressResponse.setId("idMock");
    addressResponse.setProvinces("Hai Duong");
    addressResponse.setDistricts("Kim Thanh");
    addressResponse.setWards("Tam Ky");
    addressResponse.setDetail("Tam Ky Kim Thanh Hai Duong");
    return addressResponse;
  }

  @Test
  public void testCreate_WhenCreateSuccess_Return201Body() throws Exception {
    AddressRequest addressRequest = mockAddressRequest();
    AddressResponse addressResponse = mockFacadeResponse();
    Mockito.when(addressFacadeService.createAddress(addressRequest)).thenReturn(addressResponse);
    Mockito.when(messageService.getMessage(CREATE_ADDRESS_SUCCESS, "en")).
          thenReturn("Create address success");
    MvcResult mvcResult = mockMvc.perform(
                post("/api/v1/addresses")
                      .contentType("application/json")
                      .content(objectMapper.writeValueAsBytes(addressRequest)))
          .andDo(print())
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.message")
                .value("Create address success"))
          .andReturn();
    String responseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
    Assertions.assertEquals(responseBody,
          objectMapper.writeValueAsString(addressController.create(addressRequest, "en")));
  }

  @Test
  public void testCreate_WhenProvinceCodeNotFound_Return404ProvinceNotFound() throws Exception {
    AddressRequest addressRequest = mockAddressRequest();
    mockFacadeResponse();
    Mockito.when(addressFacadeService.createAddress(addressRequest)).
          thenThrow(new AddressNotFoundException(true, false, false));
    mockMvc.perform(
                post("/api/v1/addresses")
                      .contentType("application/json")
                      .content(objectMapper.writeValueAsBytes(addressRequest)))
          .andDo(print())
          .andExpect(status().isNotFound())
          .andExpect(jsonPath("$.data.code")
                .value("com.ncsgroup.shipment.server.exception.address.AddressNotFoundException.Province"))
          .andReturn();
  }

  @Test
  public void testCreate_WhenDistrictCodeNotFound_Return404DistrictNotFound() throws Exception {
    AddressRequest addressRequest = mockAddressRequest();
    mockFacadeResponse();
    Mockito.when(addressFacadeService.createAddress(addressRequest)).
          thenThrow(new AddressNotFoundException(false, true, false));
    mockMvc.perform(
                post("/api/v1/addresses")
                      .contentType("application/json")
                      .content(objectMapper.writeValueAsBytes(addressRequest)))
          .andDo(print())
          .andExpect(status().isNotFound())
          .andExpect(jsonPath("$.data.code")
                .value("com.ncsgroup.shipment.server.exception.address.AddressNotFoundException.District"))
          .andReturn();
  }

  @Test
  public void testCreate_WhenWardCodeNotFound_Return404WardNotFound() throws Exception {
    AddressRequest addressRequest = mockAddressRequest();
    Mockito.when(addressFacadeService.createAddress(addressRequest)).
          thenThrow(new AddressNotFoundException(false, false, true));
    mockMvc.perform(
                post("/api/v1/addresses")
                      .contentType("application/json")
                      .content(objectMapper.writeValueAsBytes(addressRequest)))
          .andDo(print())
          .andExpect(status().isNotFound())
          .andExpect(jsonPath("$.data.code")
                .value("com.ncsgroup.shipment.server.exception.address.AddressNotFoundException.Ward"))
          .andReturn();
  }

  @Test
  public void testList_WhenIsAll_ReturnResponseBody() throws Exception {
    AddressResponse addressResponse = mockAddressResponse();
    List<AddressResponse> list = new ArrayList<>();
    list.add(addressResponse);

    PageResponse<AddressResponse> mock = new PageResponse<>();
    mock.setContent(list);
    mock.setAmount(list.size());

    Mockito.when(messageService.getMessage(LIST_ADDRESS, "en")).thenReturn("Get Address Success");
    Mockito.when(addressService.list("null", 10, 0, true)).thenReturn(mock);

    MvcResult mvcResult = mockMvc.perform(get("/api/v1/addresses")
                .param("keyword", "")
                .param("size", String.valueOf(10))
                .param("page", String.valueOf(0))
                .param("all", String.valueOf(true)))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.message").value("Get Address Success"))
          .andReturn();
    String responseBody = mvcResult.getResponse().getContentAsString();
    Assertions.assertEquals(responseBody, objectMapper.writeValueAsString(addressController.list("", 10, 0, true, "en")));
  }

  @Test
  public void testList_WhenSearchByKeyWord_ReturnResponseBody() throws Exception {
    AddressResponse addressResponse = mockAddressResponse();
    List<AddressResponse> list = new ArrayList<>();
    list.add(addressResponse);

    PageResponse<AddressResponse> mock = new PageResponse<>();
    mock.setContent(list);
    mock.setAmount(list.size());

    Mockito.when(messageService.getMessage(LIST_ADDRESS, "en")).thenReturn("Get Address Success");
    Mockito.when(addressService.list("Tam Ky", 10, 0, false)).thenReturn(mock);

    MvcResult mvcResult = mockMvc.perform(get("/api/v1/addresses")
                .param("keyword", "Tam Ky")
                .param("size", String.valueOf(10))
                .param("page", String.valueOf(0))
                .param("all", String.valueOf(false)))
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.message").value("Get Address Success"))
          .andReturn();
    String responseBody = mvcResult.getResponse().getContentAsString();
    Assertions.assertEquals(responseBody, objectMapper.writeValueAsString(addressController.list("Tam Ky", 10, 0, false, "en")));
  }
  @Test
  public void testDetail_WhenIdNotFound_Return404AddressNotFound() throws Exception {
    Mockito.when(addressService.detail(mockId)).thenThrow(new AddressNotFoundException());
    mockMvc.perform(
                get("/api/v1/addresses/{id}", mockId))
          .andExpect(status().isNotFound())
          .andExpect(jsonPath("$.data.code")
                .value("com.ncsgroup.shipment.server.exception.address.AddressNotFoundException"))
          .andReturn();
  }

  @Test
  public void testDetail_WhenSuccess_Return200ResponseBody() throws Exception {
    AddressResponse response = addressResponse();
    Mockito.when(addressService.detail(mockId)).thenReturn(response);
    Mockito.when(messageService.getMessage(DETAIL_ADDRESS, "en")).thenReturn("Get Detail Address Success");
    MvcResult mvcResult = mockMvc.perform(
                get("/api/v1/addresses/{id}", mockId))
          .andExpect(jsonPath("$.message")
                .value("Get Detail Address Success"))
          .andReturn();
    String responseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
    Assertions.assertEquals(responseBody,
          objectMapper.writeValueAsString(addressController.detail(mockId, "en")));
  }
}
