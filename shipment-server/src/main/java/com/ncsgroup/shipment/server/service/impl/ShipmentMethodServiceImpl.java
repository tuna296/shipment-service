package com.ncsgroup.shipment.server.service.impl;

import com.ncsgroup.shipment.server.dto.shipmentmethod.ShipmentMethodResponse;
import com.ncsgroup.shipment.server.entity.ShipmentMethod;;
import com.ncsgroup.shipment.server.repository.ShipmentMethodRepository;
import com.ncsgroup.shipment.server.service.ShipmentMethodService;
import com.ncsgroup.shipment.server.service.base.BaseServiceImpl;
import dto.ShipmentMethodRequest;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class ShipmentMethodServiceImpl extends BaseServiceImpl<ShipmentMethod> implements ShipmentMethodService {
    private final ShipmentMethodRepository repository;
    public ShipmentMethodServiceImpl(ShipmentMethodRepository repository) {
        super(repository);
        this.repository = repository;
    }

    @Override
    @Transactional
    public ShipmentMethodResponse create(ShipmentMethodRequest request) {
        log.info("(create) request: {}", request);
        ShipmentMethod shipmentMethod = ShipmentMethod.from(
                request.getName(),
                request.getDescription(),
                request.getPricePerKilometer()
        );
        repository.save(shipmentMethod);

      return ShipmentMethodResponse.from(
                shipmentMethod.getName(),
                shipmentMethod.getDescription(),
                shipmentMethod.getPricePerKilometer()
        );
    }
}
