package com.ncsgroup.shipment.server.controller;

import com.ncsgroup.shipment.server.dto.PageResponse;
import com.ncsgroup.shipment.server.dto.address.AddressResponse;
import com.ncsgroup.shipment.server.dto.shipment.ShipmentResponse;
import com.ncsgroup.shipment.server.dto.shipmentmethod.ShipmentMethodResponse;
import com.ncsgroup.shipment.server.exception.address.AddressNotFoundException;
import com.ncsgroup.shipment.server.exception.shipment.ShipmentNotFoundException;
import com.ncsgroup.shipment.server.exception.shipmentmethod.ShipmentMethodNotFoundException;
import com.ncsgroup.shipment.server.facade.ShipmentFacadeService;
import com.ncsgroup.shipment.server.service.MessageService;
import com.ncsgroup.shipment.client.dto.ShipmentRequest;

import static com.ncsgroup.shipment.server.constanst.Constants.MessageCode.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.ncsgroup.shipment.server.service.ShipmentService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

@WebMvcTest(ShipmentController.class)
public class ShipmentControllerTest {
  @Autowired
  private MockMvc mockMvc;
  @MockBean
  private ShipmentFacadeService shipmentFacadeService;
  @MockBean
  private MessageService messageService;
  @Autowired
  private ShipmentController shipmentController;
  @Autowired
  ObjectMapper objectMapper;
  @MockBean
  ShipmentService shipmentService;

  private ShipmentRequest mockShipmentRequest() {
    ShipmentRequest request = new ShipmentRequest(
          "orderId",
          "fromAddressId",
          "toAddressId",
          250000.0,
          "shipmentMethodId"
    );
    return request;
  }

  private ShipmentMethodResponse mockShipmentMethodResponse() {
    ShipmentMethodResponse response = new ShipmentMethodResponse();
    response.setId("shipmentMethodId");
    response.setName("Giao hang nhanh");
    response.setDescription("Van chuyen trong ngay");
    response.setPricePerKilometer(20000.0);
    return response;
  }

  private AddressResponse mockFromAddressResponse() {
    AddressResponse addressResponse = new AddressResponse();
    addressResponse.setId("fromAddressId");
    addressResponse.setProvinces("30");
    addressResponse.setDistricts("293");
    addressResponse.setWards("10081");
    addressResponse.setDetail("Tam Ky Kim Thanh Hai Duong");
    return addressResponse;
  }

  private AddressResponse mockToAddressResponse() {
    AddressResponse addressResponse = new AddressResponse();
    addressResponse.setId("toAddressId");
    addressResponse.setProvinces("30");
    addressResponse.setDistricts("293");
    addressResponse.setWards("10081");
    addressResponse.setDetail("Tam Ky Kim Thanh Hai Duong");
    return addressResponse;
  }

  private ShipmentResponse mockShipmentResponse() {
    ShipmentMethodResponse shipmentMethodResponse = mockShipmentMethodResponse();
    AddressResponse fromAddress = mockFromAddressResponse();
    AddressResponse toAddress = mockToAddressResponse();
    ShipmentResponse response = new ShipmentResponse(
          "idShipment",
          "SHIP01",
          25000.0,
          shipmentMethodResponse,
          fromAddress,
          toAddress
    );
    return response;
  }

  private ShipmentResponse shipmentResponse() {
    ShipmentResponse response = new ShipmentResponse(
          "idShipment",
          "SHIP01",
          20000.0
    );
    return response;
  }

  @Test
  void testCreateShipment_WhenAddressNotFound_ReturnAddressNotFoundException() throws Exception {
    ShipmentRequest request = mockShipmentRequest();
    Mockito.when(shipmentFacadeService.create(Mockito.any(ShipmentRequest.class))).thenThrow(new AddressNotFoundException());
    Mockito.when(messageService.getMessage(CREATE_SHIPMENT_SUCCESS, "en")).thenReturn("Create shipment success");
    mockMvc.perform(post("/api/v1/shipments")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
          .andDo(print())
          .andExpect(status().isNotFound())
          .andExpect(jsonPath("$.data.code")
                .value("com.ncsgroup.shipment.server.exception.address.AddressNotFoundException"))
          .andReturn();
  }

  @Test
  void testCreateShipment_WhenShipmentMethodNotFound_ReturnShipmentMethodNotFoundException() throws Exception {
    ShipmentRequest request = mockShipmentRequest();
    Mockito.when(shipmentFacadeService.create(request)).thenThrow(new ShipmentMethodNotFoundException());
    Mockito.when(messageService.getMessage(CREATE_SHIPMENT_SUCCESS, "en")).thenReturn("Create shipment success");
    mockMvc.perform(post("/api/v1/shipments")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
          .andDo(print())
          .andExpect(status().isNotFound())
          .andExpect(jsonPath("$.data.code")
                .value("com.ncsgroup.shipment.server.exception.shipmentmethod.ShipmentMethodNotFoundException"));
  }

  @Test
  void testCreateShipment_WhenSuccess_Return200Body() throws Exception {
    ShipmentRequest request = mockShipmentRequest();
    ShipmentResponse mockResponse = mockShipmentResponse();
    Mockito.when(shipmentFacadeService.create(request)).thenReturn(mockResponse);
    Mockito.when(messageService.getMessage(CREATE_SHIPMENT_SUCCESS, "en")).thenReturn("Create shipment success");
    MvcResult mvcResult = mockMvc.perform(
                post("/api/v1/shipments")
                      .contentType("application/json")
                      .content(objectMapper.writeValueAsBytes(request)))
          .andDo(print())
          .andExpect(status().isCreated())
          .andExpect(jsonPath("$.message").value("Create shipment success"))
          .andReturn();
    String responseBody = mvcResult.getResponse().getContentAsString();
    Assertions.assertEquals(responseBody,
          objectMapper.writeValueAsString(shipmentController.create(request, "en")));
  }

  @Test
  void testUpdateShipment_WhenAddressNotFound_ReturnAddressNotFoundException() throws Exception {
    ShipmentRequest request = mockShipmentRequest();
    Mockito.when(shipmentFacadeService.update(request, "idShipment")).thenThrow(new AddressNotFoundException());
    Mockito.when(messageService.getMessage(UPDATE_SHIPMENT_SUCCESS, "en")).thenReturn("Update shipment success");
    mockMvc.perform(put("/api/v1/shipments/{id}", "idShipment")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
          .andDo(print())
          .andExpect(status().isNotFound())
          .andExpect(jsonPath("$.data.code")
                .value("com.ncsgroup.shipment.server.exception.address.AddressNotFoundException"))
          .andReturn();
  }

  @Test
  void testUpdateShipment_WhenShipmentMethodNotFound_ReturnShipmentMethodNotFoundException() throws Exception {
    ShipmentRequest request = mockShipmentRequest();
    Mockito.when(shipmentFacadeService.update(request, "idShipment")).thenThrow(new ShipmentMethodNotFoundException());
    Mockito.when(messageService.getMessage(UPDATE_SHIPMENT_SUCCESS, "en")).thenReturn("Update shipment success");
    mockMvc.perform(put("/api/v1/shipments/{id}", "idShipment")
                .contentType("application/json")
                .content(objectMapper.writeValueAsString(request)))
          .andDo(print())
          .andExpect(status().isNotFound())
          .andExpect(jsonPath("$.data.code")
                .value("com.ncsgroup.shipment.server.exception.shipmentmethod.ShipmentMethodNotFoundException"));
  }

  @Test
  void testUpdateShipment_WhenSuccess_Return200Body() throws Exception {
    ShipmentRequest request = mockShipmentRequest();
    ShipmentResponse mockResponse = mockShipmentResponse();
    Mockito.when(shipmentFacadeService.update(request, "idShipment")).thenReturn(mockResponse);
    Mockito.when(messageService.getMessage(UPDATE_SHIPMENT_SUCCESS, "en")).thenReturn("Update shipment success");
    MvcResult mvcResult = mockMvc.perform(
                put("/api/v1/shipments/{id}", "idShipment")
                      .contentType("application/json")
                      .content(objectMapper.writeValueAsBytes(request)))
          .andDo(print())
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.message").value("Update shipment success"))
          .andReturn();
    String responseBody = mvcResult.getResponse().getContentAsString();
    Assertions.assertEquals(responseBody,
          objectMapper.writeValueAsString(shipmentController.update(request, "idShipment", "en")));
  }

  @Test
  void testDeleteShipment_WhenShipmentNotFound_ReturnShipmentNotFoundException() throws Exception {
    Mockito.doThrow(new ShipmentNotFoundException()).when(shipmentService).delete("idShipment");
    mockMvc.perform(delete("/api/v1/shipments/{id}", "idShipment")
                .contentType("application/json"))
          .andDo(print())
          .andExpect(status().isNotFound())
          .andExpect(jsonPath("$.data.code")
                .value("com.ncsgroup.shipment.server.exception.shipment.ShipmentNotFoundException"));
  }

  @Test
  void testDeleteShipment_WhenSuccess_ReturnResponseBody() throws Exception {
    Mockito.doNothing().when(shipmentService).delete("idShipment");
    Mockito.when(messageService.getMessage(DELETE_SHIPMENT_SUCCESS, "en")).thenReturn("Delete shipment successfully");
    mockMvc.perform(delete("/api/v1/shipments/{id}", "idShipment")
                .contentType("application/json"))
          .andDo(print())
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.message")
                .value("Delete shipment successfully"));
  }

  @Test
  void testDetailShipment_WhenSuccess_Return200Body() throws Exception {
    ShipmentRequest request = mockShipmentRequest();
    ShipmentResponse mockResponse = mockShipmentResponse();
    Mockito.when(shipmentFacadeService.detail("idShipment")).thenReturn(mockResponse);
    Mockito.when(messageService.getMessage(DETAIL_SHIPMENT_SUCCESS, "en")).thenReturn("Get detail shipment success");
    MvcResult mvcResult = mockMvc.perform(
                get("/api/v1/shipments/{id}", "idShipment")
                      .contentType("application/json")
                      .content(objectMapper.writeValueAsBytes(request)))
          .andDo(print())
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.message").value("Get detail shipment success"))
          .andReturn();
    String responseBody = mvcResult.getResponse().getContentAsString();
    Assertions.assertEquals(responseBody,
          objectMapper.writeValueAsString(shipmentController.detail("idShipment", "en")));
  }

  @Test
  void testListShipment_WhenIsAllTrue_Return200Body() throws Exception {
    ShipmentResponse shipmentResponse = shipmentResponse();
    List<ShipmentResponse> list = new ArrayList<>();
    list.add(shipmentResponse);

    PageResponse<ShipmentResponse> mockResponse = new PageResponse<>();
    mockResponse.setContent(list);
    mockResponse.setAmount(list.size());

    Mockito.when(shipmentService.list("", 10, 0, true)).thenReturn(mockResponse);
    Mockito.when(messageService.getMessage(LIST_SHIPMENT_SUCCESS, "en")).thenReturn("Get list shipment success");
    MvcResult mvcResult = mockMvc.perform(
                get("/api/v1/shipments")
                      .param("keyword", "")
                      .param("size", String.valueOf(10))
                      .param("page", String.valueOf(0))
                      .param("all", String.valueOf(true)))
          .andDo(print())
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.message").value("Get list shipment success"))
          .andReturn();
    String responseBody = mvcResult.getResponse().getContentAsString();
    Assertions.assertEquals(responseBody,
          objectMapper.writeValueAsString(shipmentController.list("", 10, 0, true, "en")));
  }
  @Test
  void testListShipment_WhenSearchByKeyword_Return200Body() throws Exception {
    ShipmentResponse shipmentResponse = shipmentResponse();
    List<ShipmentResponse> list = new ArrayList<>();
    list.add(shipmentResponse);

    PageResponse<ShipmentResponse> mockResponse = new PageResponse<>();
    mockResponse.setContent(list);
    mockResponse.setAmount(list.size());

    Mockito.when(shipmentService.list("SHIP01", 10, 0, false)).thenReturn(mockResponse);
    Mockito.when(messageService.getMessage(LIST_SHIPMENT_SUCCESS, "en")).thenReturn("Get list shipment success");
    MvcResult mvcResult = mockMvc.perform(
                get("/api/v1/shipments")
                      .param("keyword", "SHIP01")
                      .param("size", String.valueOf(10))
                      .param("page", String.valueOf(0))
                      .param("all", String.valueOf(false)))
          .andDo(print())
          .andExpect(status().isOk())
          .andExpect(jsonPath("$.message").value("Get list shipment success"))
          .andReturn();
    String responseBody = mvcResult.getResponse().getContentAsString();
    Assertions.assertEquals(responseBody,
          objectMapper.writeValueAsString(shipmentController.list("SHIP01", 10, 0, false, "en")));
  }
}
