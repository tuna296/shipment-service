package com.ncsgroup.shipment.server.repository;

import com.ncsgroup.shipment.server.entity.ShipmentMethod;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ShipmentMethodRepository extends BaseRepository<ShipmentMethod> {
    boolean existsByName(String name);
}