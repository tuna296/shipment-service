package com.ncsgroup.shipment.server.service;

import com.ncsgroup.shipment.server.configuration.ShipmentTestConfiguration;
import com.ncsgroup.shipment.server.dto.shipmentmethod.ShipmentMethodResponse;
import com.ncsgroup.shipment.server.entity.ShipmentMethod;
import com.ncsgroup.shipment.server.exception.shipmentmethod.ShipmentMethodAlreadyExistException;
import com.ncsgroup.shipment.server.exception.shipmentmethod.ShipmentMethodNotFoundException;
import com.ncsgroup.shipment.server.repository.ShipmentMethodRepository;
import dto.ShipmentMethodRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;

import java.util.Optional;

@WebMvcTest(ShipmentMethodService.class)
@ContextConfiguration(classes = ShipmentTestConfiguration.class)
public class ShipmentMethodServiceTest {
    @MockBean
    private ShipmentMethodRepository repository;
    @Autowired
    private ShipmentMethodService shipmentMethodService;
    private static final String mockId = "abc";

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
        Assertions.assertThatThrownBy(() -> shipmentMethodService.create(mockRequest)).
                isInstanceOf(ShipmentMethodAlreadyExistException.class);
    }

    @Test
    public void testCreate_WhenCreateShipmentMethod_Successfully() {
        ShipmentMethodRequest mockRequest = mockShipmentMethodRequest();
        Mockito.when(repository.existsByName(mockRequest.getName())).thenReturn(false);
        ShipmentMethodResponse response = shipmentMethodService.create(mockRequest);
        Assertions.assertThat(mockRequest.getName()).isEqualTo(response.getName());
        Assertions.assertThat(mockRequest.getDescription()).isEqualTo(response.getDescription());
        Assertions.assertThat(mockRequest.getPricePerKilometer()).isEqualTo(response.getPricePerKilometer());
    }

//failed
    @Test
    public void testUpdate_WhenNameShipmentMethodAlreadyExists_ReturnAlreadyExistsException() throws Exception {
        ShipmentMethodRequest mockRequest = mockShipmentMethodRequest();
        ShipmentMethod mockEntity = mockshipmentMethod(mockRequest);
        mockEntity.setId(mockId);
        Mockito.when(repository.findById(mockId)).thenReturn(Optional.of(mockEntity));
        Mockito.when(repository.existsByName(mockRequest.getName())).thenReturn(true);
        Assertions.assertThatThrownBy(() -> shipmentMethodService.update(mockId, mockRequest)).
                isInstanceOf(ShipmentMethodAlreadyExistException.class);
    }


    @Test
    public void testUpdate_WhenIdNotFound_ReturnShipmentMethodNotFound() {
        ShipmentMethodRequest mockRequest = mockShipmentMethodRequest();
        Mockito.when(repository.findById(mockId)).thenThrow(ShipmentMethodNotFoundException.class);
        Assertions.assertThatThrownBy(() -> shipmentMethodService.update(mockId, mockRequest)).
                isInstanceOf(ShipmentMethodNotFoundException.class);
    }

    @Test
    public void testUpdate_WhenUpdateShipmentSuccess_ReturnResponseBody() {
        ShipmentMethodRequest mockRequest = mockShipmentMethodRequest();
        ShipmentMethod mockEntity = mockshipmentMethod(mockRequest);
        mockEntity.setId(mockId);
        Mockito.when(repository.findById(mockId)).thenReturn(Optional.of(mockEntity));
        Mockito.when(repository.save(mockEntity)).thenReturn(mockEntity);
        ShipmentMethodResponse response = shipmentMethodService.update(mockId, mockRequest);
        Assertions.assertThat(mockEntity.getName()).isEqualTo(response.getName());
        Assertions.assertThat(mockEntity.getDescription()).isEqualTo(response.getDescription());
        Assertions.assertThat(mockEntity.getPricePerKilometer()).isEqualTo(response.getPricePerKilometer());
    }

    @Test
    void testList_WhenAllTrue_ReturnsShipmentMethodPageResponse() {
    }
}


