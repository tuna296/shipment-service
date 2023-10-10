package com.ncsgroup.shipment.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ncsgroup.shipment.server.dto.shipmentmethod.ShipmentMethodResponse;
import com.ncsgroup.shipment.server.entity.ShipmentMethod;
import com.ncsgroup.shipment.server.exception.shipmentmethod.ShipmentMethodAlreadyExistException;
import com.ncsgroup.shipment.server.exception.shipmentmethod.ShipmentMethodNotFoundException;
import com.ncsgroup.shipment.server.service.MessageService;
import com.ncsgroup.shipment.server.service.ShipmentMethodService;
import dto.ShipmentMethodRequest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static com.ncsgroup.shipment.server.constanst.ProfilingConstants.MessageCode.CREATE_SHIPMENT_METHOD_SUCCESS;
import static com.ncsgroup.shipment.server.constanst.ProfilingConstants.MessageCode.UPDATE_SHIPMENT_METHOD_SUCCESS;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;

@WebMvcTest(ShipmentMethodController.class)
public class ShipmentMethodControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ShipmentMethodService shipmentMethodService;
    @MockBean
    private MessageService messageService;
    @Autowired
    private ShipmentMethodController shipmentMethodController;
    @Autowired
    ObjectMapper objectMapper;
    private static final String mockId = "mockId";

    private ShipmentMethodRequest mockRequest() {
        ShipmentMethodRequest request = new ShipmentMethodRequest();
        request.setName("van chuyen nhanh");
        request.setDescription("phuong thuc van chuyen nhanh");
        request.setPricePerKilometer(20000);
        return request;
    }

    private ShipmentMethod mockShipmentMethod(ShipmentMethodRequest request) {
        return ShipmentMethod.from(request.getName(), request.getDescription(), request.getPricePerKilometer());
    }

    @Test
    public void testCreate_WhenCreatedShipmentMethodSuccessfully_Return201() throws Exception {
        ShipmentMethodRequest mockRequest = mockRequest();
        ShipmentMethod mockShipmentMethod = mockShipmentMethod(mockRequest);
        Mockito.when(shipmentMethodService.create(mockShipmentMethod)).
                thenReturn(ShipmentMethod.from(
                        mockShipmentMethod.getName(),
                        mockShipmentMethod.getDescription(),
                        mockShipmentMethod.getPricePerKilometer()));
        Mockito.when(messageService.getMessage(CREATE_SHIPMENT_METHOD_SUCCESS, "en"))
                .thenReturn("Create shipment method successfully");
        MvcResult mvcResult = mockMvc.perform(
                        post("/api/v1/shipment-methods")
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsBytes(mockRequest)))
                .andDo(print())
                .andExpect(jsonPath("$.message")
                        .value("Create shipment method successfully"))
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        Assertions.assertEquals(201, status);
        String responseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Assertions.assertEquals(responseBody,
                objectMapper.writeValueAsString(shipmentMethodController.create(mockRequest, "en")));
    }

    @Test
    void testCreate_WhenCreatedShipmentMethodNameAlreadyExists_ReturnShipmentMethodAlreadyExists() throws Exception {
        ShipmentMethodRequest mockRequest = mockRequest();
        Mockito.when(shipmentMethodService.create(mockRequest)).thenThrow(new ShipmentMethodAlreadyExistException());
        Mockito.when(messageService.getMessage(CREATE_SHIPMENT_METHOD_SUCCESS, "en"))
                .thenReturn("Create shipment method successfully");
        mockMvc.perform(post("/api/v1/shipment-methods")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(mockRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data.code")
                        .value("com.ncsgroup.shipment.server.exception.shipmentmethod.ShipmentAlreadyExistException"));
    }

    @Test
    void tesValid_WhenInputNameShipmentMethodInvalid_Returns400BadRequest() throws Exception {
        ShipmentMethodRequest request = mockRequest();
        request.setName("");
        Mockito.when(messageService.getMessage(CREATE_SHIPMENT_METHOD_SUCCESS, "en"))
                .thenReturn("Create shipment method successfully");
        mockMvc.perform(post("/api/v1/shipment-methods")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data.code")
                        .value("name cannot be empty"));
        System.out.println(request);
    }

    @Test
    void tesValid_WhenInputDescriptionShipmentMethodInvalid_Returns400BadRequest() throws Exception {
        ShipmentMethodRequest request = mockRequest();
        request.setDescription("");
        Mockito.when(messageService.getMessage(CREATE_SHIPMENT_METHOD_SUCCESS, "en"))
                .thenReturn("Create shipment method successfully");
        mockMvc.perform(post("/api/v1/shipment-methods")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data.code")
                        .value("description cannot be empty"));
        System.out.println(request);
    }

    @Test
    void testUpdate_WhenUpdateShipmentMethodSuccessfully_Return200() throws Exception {
        ShipmentMethodRequest request = mockRequest();
        ShipmentMethod mockShipmentMethod = mockShipmentMethod(request);
        Mockito.when(shipmentMethodService.update(mockId, request)).
                thenReturn(ShipmentMethodResponse.from(
                        mockShipmentMethod.getName(),
                        mockShipmentMethod.getDescription(),
                        mockShipmentMethod.getPricePerKilometer()));
        Mockito.when(messageService.getMessage(UPDATE_SHIPMENT_METHOD_SUCCESS, "en"))
                .thenReturn("Update shipment method successfully");
        MvcResult mvcResult = mockMvc.perform(
                        put("/api/v1/shipment-methods/{id}", mockId)
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(request)))
                .andReturn();
        int status = mvcResult.getResponse().getStatus();
        Assertions.assertEquals(200, status);
        String responseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        Assertions.assertEquals(responseBody,
                objectMapper.writeValueAsString(shipmentMethodController.update(request, mockId, "en")));
    }

    @Test
    void testUpdate_WhenShipmentMethodNotFound_ReturnBadRequest() throws Exception {
        ShipmentMethodRequest request = mockRequest();
        Mockito.when(shipmentMethodService.update(mockId, request)).thenThrow(new ShipmentMethodNotFoundException());
        Mockito.when(messageService.getMessage(UPDATE_SHIPMENT_METHOD_SUCCESS, "en"))
                .thenReturn("Update shipment method successfully");
        MvcResult mvcResult = mockMvc.perform(
                        put("/api/v1/shipment-methods/{id}", mockId)
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.data.code")
                        .value("com.ncsgroup.shipment.server.exception.shipmentmethod.ShipmentMethodNotFoundException"))
                .andDo(print())
                .andReturn();
    }

    @Test
    void testUpdate_WhenNameShipmentMethodAlreadyExists__ReturnBadRequest() throws Exception {
        ShipmentMethodRequest request = mockRequest();
        Mockito.when(shipmentMethodService.update(mockId, request)).thenThrow(new ShipmentMethodAlreadyExistException());
        Mockito.when(messageService.getMessage(UPDATE_SHIPMENT_METHOD_SUCCESS, "en"))
                .thenReturn("Update shipment method successfully");
        MvcResult mvcResult = mockMvc.perform(
                        put("/api/v1/shipment-methods/{id}", mockId)
                                .contentType("application/json")
                                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.data.code")
                        .value("com.ncsgroup.shipment.server.exception.shipmentmethod.ShipmentAlreadyExistException"))
                .andReturn();
    }
}

