package com.ncsgroup.shipment.server.service;

import com.ncsgroup.shipment.server.configuration.ShipmentTestConfiguration;
import com.ncsgroup.shipment.server.dto.PageResponse;
import com.ncsgroup.shipment.server.dto.address.AddressResponse;
import com.ncsgroup.shipment.server.dto.shipment.ShipmentResponse;
import com.ncsgroup.shipment.server.dto.shipmentmethod.ShipmentMethodResponse;
import com.ncsgroup.shipment.server.entity.Shipment;
import com.ncsgroup.shipment.server.exception.shipmentmethod.ShipmentMethodNotFoundException;
import com.ncsgroup.shipment.server.repository.ShipmentRepository;
import com.ncsgroup.shipment.client.dto.ShipmentRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@WebMvcTest(ShipmentService.class)
@ContextConfiguration(classes = ShipmentTestConfiguration.class)
public class ShipmentServiceTest {
  @MockBean
  private ShipmentRepository repository;
  @Autowired
  private ShipmentService shipmentService;

  private ShipmentRequest mockShipmentRequest() {
    ShipmentRequest request = new ShipmentRequest(
          "orderId",
          "fromAddressId",
          "toAddressId",
          20000.0,
          "shipmentMethodId"
    );
    return request;
  }

  private Shipment mockShipment() {
    Shipment shipment = new Shipment(
          "SHIP01",
          "orderId",
          "fromAddressId",
          "toAddressId",
          20000.0,
          "shipmentMethodId",
          false
    );
    return shipment;
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
  void testCreateShipment_WhenCreateSuccess_Successfully() throws Exception {
    ShipmentRequest request = mockShipmentRequest();
    Shipment shipment = mockShipment();
    ShipmentResponse shipmentResponse = shipmentResponse();

    Mockito.when(repository.save(shipment)).thenReturn(shipment);
    Mockito.when(repository.find(shipment.getId())).thenReturn(shipmentResponse);

    ShipmentResponse response = shipmentService.create(request);

    Assertions.assertThat(shipment.getCode()).isEqualTo(response.getCode());
    Assertions.assertThat(shipment.getPrice()).isEqualTo(response.getPrice());
  }

  @Test
  void testUpdateShipment_WhenUpdateSuccess_Successfully() throws Exception {
    ShipmentRequest request = mockShipmentRequest();
    Shipment shipment = mockShipment();
    ShipmentResponse shipmentResponse = shipmentResponse();
    Mockito.when(repository.saveAndFlush(shipment)).thenReturn(shipment);
    Mockito.when(repository.find(shipment.getId())).thenReturn(shipmentResponse);
    Mockito.when(repository.findById(shipment.getId())).thenReturn(Optional.of(shipment));

    ShipmentResponse response = shipmentService.update(request, shipment.getId());

    Assertions.assertThat(shipment.getCode()).isEqualTo(response.getCode());
    Assertions.assertThat(shipment.getPrice()).isEqualTo(response.getPrice());
  }

  @Test
  void testDeleteShipment_WhenIdNotFound_ReturnShipmentNotFound() throws Exception {
    Mockito.when(repository.findById("test")).thenThrow(ShipmentMethodNotFoundException.class);

    Assertions.assertThatThrownBy(() -> shipmentService.delete("test"))
          .isInstanceOf(ShipmentMethodNotFoundException.class);
  }

  @Test
  void testDetailShipment_WhenIdNotFound_ReturnShipmentNotFound() throws Exception {
    Mockito.when(repository.findById("test")).thenThrow(ShipmentMethodNotFoundException.class);

    Assertions.assertThatThrownBy(() -> shipmentService.delete("test"))
          .isInstanceOf(ShipmentMethodNotFoundException.class);
  }

  @Test
  void testDetailShipment_WhenSuccess_ReturnShipmentResponse() throws Exception {
    Shipment shipment = mockShipment();
    Mockito.when(repository.findById(shipment.getId())).thenReturn(Optional.of(shipment));

    ShipmentResponse response = shipmentService.detail(shipment.getId());
    Assertions.assertThat(shipment.getCode()).isEqualTo(response.getCode());
    Assertions.assertThat(shipment.getPrice()).isEqualTo(response.getPrice());
    Assertions.assertThat(shipment.getShipmentMethodId()).isEqualTo(response.getShipmentMethod().getId());
    Assertions.assertThat(shipment.getToAddressId()).isEqualTo(response.getToAddress().getId());
    Assertions.assertThat(shipment.getFromAddressId()).isEqualTo(response.getFromAddress().getId());
  }

  @Test
  void testListShipment_WhenFindAll_ReturnShipmentResponse() throws Exception {
    ShipmentResponse mockResponse = shipmentResponse();
    List<ShipmentResponse> list = new ArrayList<>();
    list.add(mockResponse);
    Pageable pageable = PageRequest.of(0, 10);
    Page<ShipmentResponse> page = new PageImpl<>(list);
    Mockito.when(repository.findAllShipment(pageable)).thenReturn(page);
    PageResponse<ShipmentResponse> response = shipmentService.list(null, 10, 0, true);
    Assertions.assertThat(response.getAmount()).isEqualTo(list.size());
  }

  @Test
  void testListShipment_WhenSearch_ReturnShipmentResponse() throws Exception {
    ShipmentResponse mockResponse = shipmentResponse();
    List<ShipmentResponse> list = new ArrayList<>();
    list.add(mockResponse);
    Pageable pageable = PageRequest.of(0, 10);
    Page<ShipmentResponse> page = new PageImpl<>(list);
    Mockito.when(repository.searchShipment(pageable,"SHIP01")).thenReturn(page);
    PageResponse<ShipmentResponse> response = shipmentService.list("SHIP01", 10, 0, false);
    Assertions.assertThat(response.getAmount()).isEqualTo(list.size());
  }
}
