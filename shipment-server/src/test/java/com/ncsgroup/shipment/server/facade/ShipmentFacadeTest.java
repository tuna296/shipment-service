package com.ncsgroup.shipment.server.facade;

import com.ncsgroup.shipment.server.configuration.ShipmentTestConfiguration;
import com.ncsgroup.shipment.server.dto.address.AddressResponse;
import com.ncsgroup.shipment.server.dto.shipment.ShipmentResponse;
import com.ncsgroup.shipment.server.dto.shipmentmethod.ShipmentMethodResponse;
import com.ncsgroup.shipment.server.entity.Shipment;
import com.ncsgroup.shipment.server.exception.address.AddressNotFoundException;
import com.ncsgroup.shipment.server.exception.shipmentmethod.ShipmentMethodNotFoundException;
import com.ncsgroup.shipment.server.service.ShipmentMethodService;
import com.ncsgroup.shipment.server.service.ShipmentService;
import com.ncsgroup.shipment.server.service.address.AddressService;
import dto.ShipmentRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import static com.ncsgroup.shipment.server.entity.enums.ShipmentStatus.CONFIRMING;

@WebMvcTest(ShipmentFacadeService.class)
@ContextConfiguration(classes = ShipmentTestConfiguration.class)
public class ShipmentFacadeTest {
  @MockBean
  private ShipmentService shipmentService;
  @MockBean
  private AddressService addressService;
  @MockBean
  private ShipmentMethodService shipmentMethodService;
  @Autowired
  private ShipmentFacadeService shipmentFacadeService;

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
  void testCreateShipment_WhenAddressNotFound_ReturnAddressNotFound() throws Exception {
    ShipmentRequest request = mockShipmentRequest();
    Mockito.when(addressService.detail(request.getToAddressId())).thenThrow(AddressNotFoundException.class);
    Assertions.assertThatThrownBy(() -> shipmentFacadeService.create(request)).isInstanceOf(AddressNotFoundException.class);
  }

  @Test
  void testCreateShipment_WhenShipmentMethodNotFound_ReturnShipmentMethodNotFound() throws Exception {
    ShipmentRequest request = mockShipmentRequest();
    Mockito.when(shipmentMethodService.detail(request.getShipmentMethodId())).thenThrow(ShipmentMethodNotFoundException.class);
    Assertions.assertThatThrownBy(() -> shipmentFacadeService.create(request)).isInstanceOf(ShipmentMethodNotFoundException.class);
  }

  @Test
  void testCreateShipment_WhenSuccess_ReturnSuccess() throws Exception {
    ShipmentRequest request = mockShipmentRequest();
    AddressResponse fromAddress = mockFromAddressResponse();
    AddressResponse toAddress = mockToAddressResponse();
    ShipmentMethodResponse shipmentMethod = mockShipmentMethodResponse();
    ShipmentResponse mockResponseService = shipmentResponse();

    Mockito.when(addressService.detail(request.getToAddressId())).thenReturn(toAddress);
    Mockito.when(addressService.detail(request.getFromAddressId())).thenReturn(fromAddress);
    Mockito.when(shipmentMethodService.detail(request.getShipmentMethodId())).thenReturn(shipmentMethod);
    Mockito.when(shipmentService.create(request)).thenReturn(mockResponseService);

    ShipmentResponse response = shipmentFacadeService.create(request);
    Assertions.assertThat(response.getId()).isEqualTo(mockResponseService.getId());
    Assertions.assertThat(response.getCode()).isEqualTo(mockResponseService.getCode());
    Assertions.assertThat(response.getShipmentStatus()).isEqualTo(mockResponseService.getShipmentStatus());
    Assertions.assertThat(response.getPrice()).isEqualTo(mockResponseService.getPrice());
    Assertions.assertThat(response.getFromAddress()).isEqualTo(fromAddress);
    Assertions.assertThat(response.getToAddress()).isEqualTo(toAddress);
    Assertions.assertThat(response.getShipmentMethod()).isEqualTo(shipmentMethod);
  }
}
