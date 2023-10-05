package com.ncsgroup.shipment.server.service;

import com.ncsgroup.shipment.server.configuration.ShipmentTestConfiguration;
import com.ncsgroup.shipment.server.dto.shipmentmethod.ShipmentMethodResponse;
import com.ncsgroup.shipment.server.entity.ShipmentMethod;
import com.ncsgroup.shipment.server.exception.shipmentmethod.ShipmentAlreadyExistException;
import com.ncsgroup.shipment.server.repository.ShipmentMethodRepository;
import dto.ShipmentMethodRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

@WebMvcTest(ShipmentMethodService.class)
@ContextConfiguration(classes = ShipmentTestConfiguration.class)
public class ShipmentMethodServiceTest {
    @MockBean
    private ShipmentMethodRepository repository;
    @Autowired
    private ShipmentMethodService shipmentMethodService;

    private ShipmentMethodRequest mockShipmentMethodRequest() {
        ShipmentMethodRequest request = new ShipmentMethodRequest();
        request.setName("Giao hang nhanh");
        request.setDescription("Van chuyen trong ngay");
        request.setPricePerKilometer(20000);
        return request;
    }

    private ShipmentMethod mockshipmentMethod(ShipmentMethodRequest request) {

        return ShipmentMethod.from(request.getName(), request.getDescription(), request.getPricePerKilometer());
    }

    @Test
    public void testCreate_WhenNameShipmentMethodAlreadyExists_ReturnShipmentMethodAlreadyExistException() {
        ShipmentMethodRequest mockRequest = mockShipmentMethodRequest();
        Mockito.when(repository.existsByName(mockRequest.getName())).thenReturn(true);
        Assertions.assertThatThrownBy(() -> shipmentMethodService.create(mockRequest)).isInstanceOf(ShipmentAlreadyExistException.class);
    }

    @Test
    public void testCreate_WhenCreateShipmentMethod_Successfully() {
        ShipmentMethodRequest mockRequest = mockShipmentMethodRequest();
        ShipmentMethod mockEntity = mockshipmentMethod(mockRequest);
        Mockito.when(repository.existsByName(mockRequest.getName())).thenReturn(false);

        ShipmentMethodResponse response = shipmentMethodService.create(mockRequest);
        Assertions.assertThat(mockEntity.getName()).isEqualTo(response.getName());
        Assertions.assertThat(mockEntity.getDescription()).isEqualTo(response.getDescription());
        Assertions.assertThat(mockEntity.getPricePerKilometer()).isEqualTo(response.getPricePerKilometer());
    }


}


