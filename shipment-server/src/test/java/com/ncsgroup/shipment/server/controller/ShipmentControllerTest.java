package com.ncsgroup.shipment.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ncsgroup.shipment.server.dto.address.AddressResponse;
import com.ncsgroup.shipment.server.dto.shipment.ShipmentResponse;
import com.ncsgroup.shipment.server.dto.shipmentmethod.ShipmentMethodResponse;
import com.ncsgroup.shipment.server.entity.Shipment;
import com.ncsgroup.shipment.server.exception.address.AddressNotFoundException;
import com.ncsgroup.shipment.server.exception.shipmentmethod.ShipmentMethodNotFoundException;
import com.ncsgroup.shipment.server.facade.ShipmentFacadeService;
import com.ncsgroup.shipment.server.service.MessageService;
import dto.ShipmentRequest;

import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.ncsgroup.shipment.server.constanst.Constants.MessageCode.CREATE_SHIPMENT_SUCCESS;
import static com.ncsgroup.shipment.server.entity.enums.ShipmentStatus.CONFIRMING;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

  private ShipmentRequest mockShipmentRequest() {
    ShipmentRequest request = new ShipmentRequest(
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
          CONFIRMING,
          shipmentMethodResponse,
          fromAddress,
          toAddress
    );
    return response;
  }

  private Shipment mockShipment() {
    Shipment shipment = new Shipment(
          "SHIP01",
          "fromAddressId",
          "toAddressId",
          20000.0,
          "shipmentMethodId",
          CONFIRMING,
          false
    );
    return shipment;
  }

  private ShipmentResponse shipmentResponse() {
    ShipmentResponse response = new ShipmentResponse(
          "idShipment",
          "SHIP01",
          20000.0,
          CONFIRMING
    );
    return response;
  }

  @Test
  void testCreateShipment_WhenAddressNotFound_ReturnAddressNotFoundException() throws Exception {
    ShipmentRequest request = mockShipmentRequest();
    Mockito.when(shipmentFacadeService.create(request)).thenThrow(new AddressNotFoundException());
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
    Mockito.when(shipmentFacadeService.create(request)).thenThrow(ShipmentMethodNotFoundException.class);
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
    String responseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
    Assertions.assertEquals(responseBody,
          objectMapper.writeValueAsString(shipmentController.create(request, "en")));
  }

}
