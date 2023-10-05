package com.ncsgroup.shipment.server.service.impl;

import com.ncsgroup.shipment.server.dto.shipmentmethod.ShipmentMethodResponse;
import com.ncsgroup.shipment.server.entity.ShipmentMethod;;
import com.ncsgroup.shipment.server.exception.shipmentmethod.ShipmentAlreadyExistException;
import com.ncsgroup.shipment.server.exception.shipmentmethod.ShipmentMethodNotFoundException;
import com.ncsgroup.shipment.server.repository.ShipmentMethodRepository;
import com.ncsgroup.shipment.server.service.ShipmentMethodService;
import com.ncsgroup.shipment.server.service.base.BaseServiceImpl;
import dto.ShipmentMethodRequest;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

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
        checkShipmentMethodAlreadyExists(request.getName());
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

    @Override
    public ShipmentMethodResponse findById(String id) {
        log.info("(findById) id: {}", id);
        ShipmentMethod shipmentMethod = repository.findById(id).orElseThrow(ShipmentMethodNotFoundException::new);
        ShipmentMethodResponse response = ShipmentMethodResponse.from(
                shipmentMethod.getName(),
                shipmentMethod.getDescription(),
                shipmentMethod.getPricePerKilometer()
        );
        return response;
    }

    private void checkShipmentMethodAlreadyExists(String name) {
        log.info("checkShipmentMethodAlreadyExists :{}", name);
        if (repository.existsByName(name)) {
            log.error("Shipment Method AlreadyExists :{}, name");
            throw new ShipmentAlreadyExistException();
        }
    }
}
