package com.ncsgroup.shipment.server.service;

import com.ncsgroup.shipment.server.configuration.ShipmentTestConfiguration;
import com.ncsgroup.shipment.server.dto.address.AddressResponse;
import com.ncsgroup.shipment.server.dto.shipment.ShipmentResponse;
import com.ncsgroup.shipment.server.dto.shipmentmethod.ShipmentMethodResponse;
import com.ncsgroup.shipment.server.entity.Shipment;
import com.ncsgroup.shipment.server.repository.ShipmentRepository;
import dto.ShipmentRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import static com.ncsgroup.shipment.server.entity.enums.ShipmentStatus.CONFIRMING;

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
          "orderId",
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
  void testCreateShipment_WhenCreateSuccess_Successfully() throws Exception {
    ShipmentRequest request = mockShipmentRequest();
    Shipment shipment = mockShipment();
    ShipmentResponse shipmentResponse = shipmentResponse();

    Mockito.when(repository.save(shipment)).thenReturn(shipment);
    Mockito.when(repository.find(shipment.getId())).thenReturn(shipmentResponse);

    ShipmentResponse response = shipmentService.create(request);

    Assertions.assertThat(shipment.getCode()).isEqualTo(response.getCode());
    Assertions.assertThat(shipment.getPrice()).isEqualTo(response.getPrice());
    Assertions.assertThat(shipment.getShipmentStatus()).isEqualTo(response.getShipmentStatus());
  }

}
