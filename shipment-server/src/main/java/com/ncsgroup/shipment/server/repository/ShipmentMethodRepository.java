package com.ncsgroup.shipment.server.repository;

import com.ncsgroup.shipment.server.dto.address.AddressResponse;
import com.ncsgroup.shipment.server.dto.shipmentmethod.ShipmentMethodResponse;
import com.ncsgroup.shipment.server.entity.ShipmentMethod;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.data.domain.Pageable;

import java.util.List;


public interface ShipmentMethodRepository extends BaseRepository<ShipmentMethod> {
  @Query("SELECT CASE WHEN COUNT(e) > 0" +
        " THEN true ELSE false END FROM ShipmentMethod e " +
        "WHERE e.name = :name AND e.isDeleted = false")
  boolean existsByName(String name);

  @Query("SELECT CASE WHEN COUNT(e) > 0" +
        " THEN true ELSE false END FROM ShipmentMethod e " +
        "WHERE e.id = :id AND e.isDeleted = false")
  boolean existsById(String id);

  //  @Query("""
//        SELECT new com.ncsgroup.shipment.server.dto.shipmentmethod.ShipmentMethodResponse
//        (s.name,s.description,s.pricePerKilometer)
//        FROM ShipmentMethod s
//        WHERE :keyword is null or lower(s.name)
//        LIKE lower(concat('%', :keyword, '%'))
//        AND s.isDeleted = false
//        """)
//  Page<ShipmentMethodResponse> search(String keyword, Pageable pageable);
  @Query("""
            SELECT new com.ncsgroup.shipment.server.dto.shipmentmethod.ShipmentMethodResponse
            (s.id,s.name, s.description, s.pricePerKilometer)
            FROM ShipmentMethod s
            WHERE (:keyword is null or lower(s.name) LIKE lower(concat('%', :keyword, '%')))
            AND s.isDeleted = false
        """)
  Page<ShipmentMethodResponse> search(String keyword, Pageable pageable);

  @Query("""
        SELECT new com.ncsgroup.shipment.server.dto.shipmentmethod.ShipmentMethodResponse
        (s.id,s.name,s.description,s.pricePerKilometer)
        FROM ShipmentMethod s
        WHERE s.isDeleted = false
        """)
  Page<ShipmentMethodResponse> findAllShipmentMethod(Pageable pageable);

  @Modifying
  @Transactional
  @Query("UPDATE ShipmentMethod w SET w.isDeleted = true WHERE w.id = :id")
  void deleteById(String id);
}