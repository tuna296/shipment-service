package com.ncsgroup.shipment.server.configuration;

import com.ncsgroup.shipment.server.repository.ShipmentMethodRepository;
import com.ncsgroup.shipment.server.repository.address.DistrictRepository;
import com.ncsgroup.shipment.server.repository.address.ProvinceRepository;
import com.ncsgroup.shipment.server.service.MessageService;
import com.ncsgroup.shipment.server.service.ShipmentMethodService;
import com.ncsgroup.shipment.server.service.address.DistrictService;
import com.ncsgroup.shipment.server.service.address.ProvinceService;
import com.ncsgroup.shipment.server.service.address.impl.DistrictServiceImpl;
import com.ncsgroup.shipment.server.service.address.impl.ProvinceServiceImpl;
import com.ncsgroup.shipment.server.service.impl.MessageServiceImpl;
import com.ncsgroup.shipment.server.service.impl.ShipmentMethodServiceImpl;
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
  public ProvinceService provinceService(ProvinceRepository repository){
    return new ProvinceServiceImpl(repository);
  }
  @Bean
  public DistrictService districtService(DistrictRepository repository){
    return new DistrictServiceImpl(repository);
  }

  @Bean
  public MessageService messageService(MessageSource messageSource) {
    return new MessageServiceImpl(messageSource);
  }

}
