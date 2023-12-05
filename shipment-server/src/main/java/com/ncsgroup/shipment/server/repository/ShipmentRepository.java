package com.ncsgroup.shipment.server.repository;

import com.ncsgroup.shipment.server.dto.shipment.ShipmentResponse;
import com.ncsgroup.shipment.server.entity.Shipment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

public interface ShipmentRepository extends BaseRepository<Shipment> {
  @Query("""
        SELECT new com.ncsgroup.shipment.server.dto.shipment.ShipmentResponse
        (s.id,s.code,s.price)
        FROM Shipment s
        WHERE s.id =:id AND s.isDeleted =false
        """)
  ShipmentResponse find(String id);

  @Transactional
  @Modifying
  @Query("""
        UPDATE Shipment s SET s.isDeleted= true where s.id = :id
        """)
  void delete(String id);

  @Query("""
                SELECT new com.ncsgroup.shipment.server.dto.shipment.ShipmentResponse
                (s.id,s.code,s.price)
                FROM Shipment s
                WHERE s.isDeleted =false
        """)
  Page<ShipmentResponse>  findAllShipment(Pageable pageable);

  @Query("""
               SELECT new com.ncsgroup.shipment.server.dto.shipment.ShipmentResponse
               (s.id,s.code,s.price)
               FROM Shipment s
               WHERE (:keyword is null or
               lower(s.code) LIKE lower(concat('%', :keyword, '%')))
               AND s.isDeleted =false
       """)
  Page<ShipmentResponse> searchShipment(Pageable pageable, String keyword);

}
