package com.ncsgroup.shipment.server.service;

import com.ncsgroup.shipment.server.configuration.ShipmentTestConfiguration;
import com.ncsgroup.shipment.server.dto.address.AddressResponse;
import com.ncsgroup.shipment.server.dto.shipment.ShipmentResponse;
import com.ncsgroup.shipment.server.dto.shipmentmethod.ShipmentMethodResponse;
import com.ncsgroup.shipment.server.entity.Shipment;
import com.ncsgroup.shipment.server.repository.ShipmentRepository;
import com.ncsgroup.shipment.client.dto.ShipmentRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

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
          20000.0,
          shipmentMethodResponse,
          fromAddress,
          toAddress
    );
    return response;
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

    ShipmentResponse response = shipmentService.update(request,shipment.getId());

    Assertions.assertThat(shipment.getCode()).isEqualTo(response.getCode());
    Assertions.assertThat(shipment.getPrice()).isEqualTo(response.getPrice());
  }
}
