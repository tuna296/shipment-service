package com.ncsgroup.shipment.server.controller.address;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ncsgroup.shipment.server.dto.address.AddressResponse;
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

import static com.ncsgroup.shipment.server.constanst.Constants.MessageCode.CREATE_ADDRESS_SUCCESS;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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


  private AddressRequest mockAddressRequest() {
    AddressRequest addressRequest = new AddressRequest();
    addressRequest.setProvinceCode("30");
    addressRequest.setDistrictCode("293");
    addressRequest.setWardCode("10081");
    addressRequest.setDetail("Tam Ky Kim Thanh Hai Duong");
    return addressRequest;
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

  @Test
  public void testCreate_WhenCreateSuccess_Return201Body() throws Exception {
    AddressRequest addressRequest = mockAddressRequest();
    AddressResponse addressResponse = mockFacadeResponse();
    Mockito.when(addressFacadeService.createAddress(addressRequest)).thenReturn(addressResponse);
    Mockito.when(messageService.getMessage(CREATE_ADDRESS_SUCCESS, "en")).thenReturn("Create address success");
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
    AddressResponse addressResponse = mockFacadeResponse();
    Mockito.when(addressFacadeService.createAddress(addressRequest)).thenThrow(new AddressNotFoundException(true, false, false));
    MvcResult mvcResult = mockMvc.perform(
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
    AddressResponse addressResponse = mockFacadeResponse();
    Mockito.when(addressFacadeService.createAddress(addressRequest)).thenThrow(new AddressNotFoundException(false, true, false));
    MvcResult mvcResult = mockMvc.perform(
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
  public void testCreate_WhenWardCodeNotFound_Return404WardtNotFound() throws Exception {
    AddressRequest addressRequest = mockAddressRequest();
    AddressResponse addressResponse = mockFacadeResponse();
    Mockito.when(addressFacadeService.createAddress(addressRequest)).thenThrow(new AddressNotFoundException(false, false, true));
    MvcResult mvcResult = mockMvc.perform(
                post("/api/v1/addresses")
                      .contentType("application/json")
                      .content(objectMapper.writeValueAsBytes(addressRequest)))
          .andDo(print())
          .andExpect(status().isNotFound())
          .andExpect(jsonPath("$.data.code")
                .value("com.ncsgroup.shipment.server.exception.address.AddressNotFoundException.Ward"))
          .andReturn();
  }

}
