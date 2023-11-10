package com.ncsgroup.shipment.server.service;

import com.ncsgroup.shipment.server.configuration.ShipmentTestConfiguration;
import com.ncsgroup.shipment.server.dto.PageResponse;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
    request.setPricePerKilometer(20000.0);
    return request;
  }
  private ShipmentMethodResponse mockShipmentMethodResponse() {
    ShipmentMethodResponse response = new ShipmentMethodResponse();
    response.setName("Giao hang nhanh");
    response.setDescription("Van chuyen trong ngay");
    response.setPricePerKilometer(20000.0);
    return response;
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
    List<ShipmentMethodResponse> list = new ArrayList<>();
    list.add(mockShipmentMethodResponse());

    Pageable pageable = PageRequest.of(0, 10);
    Page<ShipmentMethodResponse> mockPage = new PageImpl<>(list);

    Mockito.when(repository.findAllShipmentMethod(pageable)).thenReturn(mockPage);

    PageResponse<ShipmentMethodResponse> response = shipmentMethodService.list(null, 10, 0, true);
    Assertions.assertThat(response.getAmount()).isEqualTo(list.size());
  }

  @Test
  void testList_WhenAllFalse_ReturnsShipmentMethodPageResponse() {
    List<ShipmentMethodResponse> list = new ArrayList<>();
    list.add(mockShipmentMethodResponse());

    Pageable pageable = PageRequest.of(0, 10);
    Page<ShipmentMethodResponse> mockPage = new PageImpl<>(list);

    Mockito.when(repository.search("Giao Hang",pageable)).thenReturn(mockPage);

    PageResponse<ShipmentMethodResponse> response = shipmentMethodService.list("Giao Hang", 10, 0, false);
    Assertions.assertThat(response.getAmount()).isEqualTo(list.size());
  }


  @Test
  void testDelete_WhenIdNotFound_ReturnsShipmentMethodNotFoundException() throws Exception {
    Mockito.when(repository.existsById(mockId)).thenReturn(false);
    Assertions.assertThatThrownBy(() -> shipmentMethodService.delete(mockId)).isInstanceOf(ShipmentMethodNotFoundException.class);
  }

  @Test
  void test_DeleteShipmentMethod_WhenIdIsDeleteReturnsShipmentMethodNotFoundException() throws Exception {
    ShipmentMethodRequest request = mockShipmentMethodRequest();
    ShipmentMethod mockEntity = mockShipmentMethod(request);
    mockEntity.setId(mockId);
    Mockito.when(repository.findById(mockId)).thenReturn(Optional.of(mockEntity));
    Assertions.assertThatThrownBy(() -> shipmentMethodService.delete(mockId)).isInstanceOf(ShipmentMethodNotFoundException.class);
  }
}


