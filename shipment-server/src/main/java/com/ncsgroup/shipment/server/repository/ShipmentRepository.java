package com.ncsgroup.shipment.server.repository;

import com.ncsgroup.shipment.server.dto.shipment.ShipmentResponse;
import com.ncsgroup.shipment.server.entity.Shipment;
import org.springframework.data.jpa.repository.Query;

public interface ShipmentRepository extends BaseRepository<Shipment> {
  @Query("""
        SELECT new com.ncsgroup.shipment.server.dto.shipment.ShipmentResponse
        (s.id,s.code,s.price, s.shipmentStatus)
        FROM Shipment s
        WHERE s.id =:id AND s.isDeleted =false
        """)
  ShipmentResponse find(String id);
}
