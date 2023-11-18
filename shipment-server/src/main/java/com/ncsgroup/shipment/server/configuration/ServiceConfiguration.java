package com.ncsgroup.shipment.server.configuration;

import com.ncsgroup.shipment.server.facade.AddressFacadeService;
import com.ncsgroup.shipment.server.facade.ShipmentFacadeService;
import com.ncsgroup.shipment.server.facade.impl.AddressFacadeServiceImpl;
import com.ncsgroup.shipment.server.facade.impl.ShipmentFacadeImpl;
import com.ncsgroup.shipment.server.repository.ShipmentMethodRepository;
import com.ncsgroup.shipment.server.repository.ShipmentRepository;
import com.ncsgroup.shipment.server.repository.address.AddressRepository;
import com.ncsgroup.shipment.server.repository.address.DistrictRepository;
import com.ncsgroup.shipment.server.repository.address.ProvinceRepository;
import com.ncsgroup.shipment.server.repository.address.WardRepository;
import com.ncsgroup.shipment.server.service.MessageService;
import com.ncsgroup.shipment.server.service.ShipmentMethodService;
import com.ncsgroup.shipment.server.service.ShipmentService;
import com.ncsgroup.shipment.server.service.address.AddressService;
import com.ncsgroup.shipment.server.service.address.DistrictService;
import com.ncsgroup.shipment.server.service.address.ProvinceService;
import com.ncsgroup.shipment.server.service.address.WardService;
import com.ncsgroup.shipment.server.service.address.impl.AddressServiceImpl;
import com.ncsgroup.shipment.server.service.address.impl.DistrictServiceImpl;
import com.ncsgroup.shipment.server.service.address.impl.ProvinceServiceImpl;
import com.ncsgroup.shipment.server.service.address.impl.WardServiceImpl;
import com.ncsgroup.shipment.server.service.impl.MessageServiceImpl;
import com.ncsgroup.shipment.server.service.impl.ShipmentMethodServiceImpl;
import com.ncsgroup.shipment.server.service.impl.ShipmentServiceImpl;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ServiceConfiguration {
  @Bean
  public ShipmentMethodService shipmentMethodService(ShipmentMethodRepository repository) {
    return new ShipmentMethodServiceImpl(repository);
  }

  @Bean
  public ProvinceService provinceService(ProvinceRepository repository) {
    return new ProvinceServiceImpl(repository);
  }

  @Bean
  public DistrictService districtService(DistrictRepository repository) {
    return new DistrictServiceImpl(repository);
  }

  @Bean
  public WardService wardService(WardRepository repository) {
    return new WardServiceImpl(repository);
  }

  @Bean
  public AddressService addressService(AddressRepository addressRepository) {
    return new AddressServiceImpl(addressRepository);
  }

  @Bean
  public AddressFacadeService addressFacadeService(
        AddressService addressService,
        DistrictService districtService,
        ProvinceService provinceService,
        WardService wardService
  ) {
    return new AddressFacadeServiceImpl(addressService, provinceService, districtService, wardService);
  }

  @Bean
  public ShipmentService shipmentService(ShipmentRepository repository) {
    return new ShipmentServiceImpl(repository);
  }

  @Bean
  public ShipmentFacadeService shipmentFacadeService(
        ShipmentService shipmentService,
        AddressService addressService,
        ShipmentMethodService shipmentMethodService
  ) {
    return new ShipmentFacadeImpl(shipmentService, addressService, shipmentMethodService);
  }

  @Bean
  public MessageService messageService(MessageSource messageSource) {
    return new MessageServiceImpl(messageSource);
  }

}
