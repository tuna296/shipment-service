package com.ncsgroup.shipment.server.repository;

import com.ncsgroup.shipment.server.entity.ShipmentMethod;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface ShipmentMethodRepository extends BaseRepository<ShipmentMethod> {
    @Query("SELECT CASE WHEN COUNT(e) > 0" +
            " THEN true ELSE false END FROM ShipmentMethod e " +
            "WHERE e.name = :name AND e.isDeleted = false")
    boolean existsByName(String name);

    @Query("SELECT CASE WHEN COUNT(e) > 0" +
            " THEN true ELSE false END FROM ShipmentMethod e " +
            "WHERE e.id = :id AND e.isDeleted = false")
    boolean existsById(String id);

}