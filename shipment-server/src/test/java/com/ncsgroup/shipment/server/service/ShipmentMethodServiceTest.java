package com.ncsgroup.shipment.server.service;

import com.ncsgroup.shipment.server.configuration.ShipmentTestConfiguration;
import com.ncsgroup.shipment.server.dto.shipmentmethod.ShipmentMethodPageResponse;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;
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

  private ShipmentMethod mockShipmentMethod(ShipmentMethodRequest request) {
    return ShipmentMethod.from(request.getName(), request.getDescription(), request.getPricePerKilometer());
  }

  @Test
  void testCreate_WhenNameShipmentMethodAlreadyExists_ReturnShipmentMethodAlreadyExistException() {
    ShipmentMethodRequest mockRequest = mockShipmentMethodRequest();
    Mockito.when(repository.existsByName(mockRequest.getName())).thenReturn(true);
    Assertions.assertThatThrownBy(() -> shipmentMethodService.create(mockRequest)).
          isInstanceOf(ShipmentMethodAlreadyExistException.class);
  }

  @Test
  void testCreate_WhenCreateShipmentMethod_Successfully() {
    ShipmentMethodRequest mockRequest = mockShipmentMethodRequest();
    ShipmentMethod mockEntity = mockShipmentMethod(mockRequest);
    Mockito.when(repository.existsByName(mockRequest.getName())).thenReturn(false);
    Mockito.when(repository.save(mockEntity)).thenReturn(mockEntity);
    ShipmentMethodResponse response = shipmentMethodService.create(mockRequest);
    Assertions.assertThat(mockEntity.getName()).isEqualTo(response.getName());
    Assertions.assertThat(mockEntity.getDescription()).isEqualTo(response.getDescription());
    Assertions.assertThat(mockEntity.getPricePerKilometer()).isEqualTo(response.getPricePerKilometer());
  }

  @Test
  void testUpdate_WhenNameShipmentMethodAlreadyExists_ReturnAlreadyExistsException() throws Exception {
    ShipmentMethodRequest mockRequest = mockShipmentMethodRequest();
    ShipmentMethod mockEntity = mockShipmentMethod(mockRequest);
    mockEntity.setId(mockId);
    mockEntity.setName("ok");
    Mockito.when(repository.findById(mockId)).thenReturn(Optional.of(mockEntity));
    Mockito.when(repository.existsByName(mockRequest.getName())).thenReturn(true);
    Assertions.assertThatThrownBy(() -> shipmentMethodService.update(mockId, mockRequest)).
          isInstanceOf(ShipmentMethodAlreadyExistException.class);
  }

  @Test
  void testUpdate_WhenIdNotFound_ReturnShipmentMethodNotFound() {
    ShipmentMethodRequest mockRequest = mockShipmentMethodRequest();
    Mockito.when(repository.findById(mockId)).thenThrow(ShipmentMethodNotFoundException.class);
    Assertions.assertThatThrownBy(() -> shipmentMethodService.update(mockId, mockRequest)).
          isInstanceOf(ShipmentMethodNotFoundException.class);
  }

  @Test
  void testUpdate_WhenUpdateShipmentSuccess_ReturnResponseBody() {
    ShipmentMethodRequest mockRequest = mockShipmentMethodRequest();
    ShipmentMethod mockEntity = mockShipmentMethod(mockRequest);
    mockEntity.setId(mockId);
    Mockito.when(repository.findById(mockId)).thenReturn(Optional.of(mockEntity));
    Mockito.when(repository.save(mockEntity)).thenReturn(mockEntity);
    ShipmentMethodResponse response = shipmentMethodService.update(mockId, mockRequest);
    Assertions.assertThat(mockEntity.getName()).isEqualTo(response.getName());
    Assertions.assertThat(mockEntity.getDescription()).isEqualTo(response.getDescription());
    Assertions.assertThat(mockEntity.getPricePerKilometer()).isEqualTo(response.getPricePerKilometer());
  }

  @Test
  void test_Update_WhenShipmentMethodIsDelete_ReturnShipmentMethodNotFoundException() throws Exception {
    ShipmentMethodRequest request = mockShipmentMethodRequest();
    ShipmentMethod mockEntity = mockShipmentMethod(request);
    mockEntity.setId(mockId);
    mockEntity.setDeleted(true);
    Mockito.when(repository.findById(mockId)).thenReturn(Optional.of(mockEntity));
    Assertions.assertThatThrownBy(() -> shipmentMethodService.update(mockId, request)).
          isInstanceOf(ShipmentMethodNotFoundException.class);
  }

  @Test
  void testList_WhenAllTrue_ReturnsShipmentMethodPageResponse() {
    ShipmentMethodRequest request = mockShipmentMethodRequest();
    ShipmentMethodRequest request1 = mockShipmentMethodRequest();
    ShipmentMethodRequest request2 = mockShipmentMethodRequest();
    List<ShipmentMethod> list = new ArrayList<>();
    list.add(mockShipmentMethod(request));
    list.add(mockShipmentMethod(request1));
    list.add(mockShipmentMethod(request2));
    Mockito.when(repository.findAll()).thenReturn(list);
    ShipmentMethodPageResponse response = shipmentMethodService.list(null, 5, 0, true);
    Assertions.assertThat(list.size()).isEqualTo(response.getCount());
  }

  @Test
  void testList_WhenAllFalse_ReturnsShipmentMethodPageResponse() {
    Pageable pageable = PageRequest.of(0, 10);
    ShipmentMethodRequest request = mockShipmentMethodRequest();
    request.setName("AnhTu");
    ShipmentMethodRequest request1 = mockShipmentMethodRequest();
    request.setName("AnhTu");
    ShipmentMethodRequest request2 = mockShipmentMethodRequest();
    List<ShipmentMethod> list = new ArrayList<>();
    list.add(mockShipmentMethod(request));
    list.add(mockShipmentMethod(request1));
    list.add(mockShipmentMethod(request2));
    Mockito.when(repository.search("AnhTu", pageable)).thenReturn(list);
    Mockito.when(repository.countSearch("AnhTu")).thenReturn(list.size());
    ShipmentMethodPageResponse response = shipmentMethodService.list("AnhTu", 10, 0, false);
    Assertions.assertThat(list.size()).isEqualTo(response.getCount());
    Assertions.assertThat(list.size()).isEqualTo(response.getShipmentMethodResponseList().size());
  }

  @Test
  void testDelete_WhenIdNotFound_ReturnsShipmentMethodNotFoundException() throws Exception {
    Mockito.when(repository.existsById(mockId)).thenReturn(false);
    Assertions.assertThatThrownBy(() -> shipmentMethodService.delete(mockId)).isInstanceOf(ShipmentMethodNotFoundException.class);
  }

  @Test
  void test_DeleteShipmentMethod_WhenIdIsDeleteReturnsShipmentMethodNotFoundException() throws Exception {
    ShipmentMethodRequest request = new ShipmentMethodRequest();
    ShipmentMethod mockEntity = mockShipmentMethod(request);
    mockEntity.setId(mockId);
    mockEntity.setDeleted(true);
    Mockito.when(repository.findById(mockId)).thenReturn(Optional.of(mockEntity));
    Assertions.assertThatThrownBy(() -> shipmentMethodService.delete(mockId)).isInstanceOf(ShipmentMethodNotFoundException.class);
  }
}


