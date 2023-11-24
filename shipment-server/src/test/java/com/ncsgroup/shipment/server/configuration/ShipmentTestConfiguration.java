package com.ncsgroup.shipment.server.configuration;

import com.ncsgroup.shipment.server.facade.AddressFacadeService;
import com.ncsgroup.shipment.server.facade.ShipmentFacadeService;
import com.ncsgroup.shipment.server.facade.impl.AddressFacadeServiceImpl;
import com.ncsgroup.shipment.server.facade.impl.ShipmentFacadeServiceImpl;
import com.ncsgroup.shipment.server.repository.ShipmentMethodRepository;
import com.ncsgroup.shipment.server.repository.ShipmentRepository;
import com.ncsgroup.shipment.server.repository.address.AddressRepository;
import com.ncsgroup.shipment.server.repository.address.DistrictRepository;
import com.ncsgroup.shipment.server.repository.address.ProvinceRepository;
import com.ncsgroup.shipment.server.repository.address.WardRepository;
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
import com.ncsgroup.shipment.server.service.impl.ShipmentMethodServiceImpl;
import com.ncsgroup.shipment.server.service.impl.ShipmentServiceImpl;
import jakarta.persistence.EntityManagerFactory;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

@TestConfiguration
@EnableJpaRepositories(basePackages = {"com.ncsgroup.shipment.server.repository"},
      entityManagerFactoryRef = "testEntityManagerFactory",
      transactionManagerRef = "testTransactionManager")
@ComponentScan(basePackages = "com.ncsgroup.shipment.server.service")
@EnableTransactionManagement

public class ShipmentTestConfiguration {
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
  public AddressService addressService(AddressRepository repository) {
    return new AddressServiceImpl(repository);
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
    return new ShipmentFacadeServiceImpl(shipmentService, addressService, shipmentMethodService);
  }

  @Bean
  public DataSource dataSource() {

    EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
    return builder.setType(EmbeddedDatabaseType.H2).build();
  }

  @Bean
  public EntityManagerFactory testEntityManagerFactory() {

    HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
    vendorAdapter.setGenerateDdl(true);

    LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
    factory.setJpaVendorAdapter(vendorAdapter);
    factory.setPackagesToScan("com.ncsgroup.shipment.server.entity");
    factory.setDataSource(dataSource());
    factory.afterPropertiesSet();
    return factory.getObject();
  }

  @Bean
  public PlatformTransactionManager testTransactionManager() {
    JpaTransactionManager txManager = new JpaTransactionManager();
    txManager.setEntityManagerFactory(testEntityManagerFactory());
    return txManager;
  }


}
