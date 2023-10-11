package com.ncsgroup.shipment.server.repository;

import com.ncsgroup.shipment.server.entity.ShipmentMethod;
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

    @Query("SELECT s FROM ShipmentMethod s WHERE :keyword is null or lower(s.name)" +
            "LIKE lower(concat('%', :keyword, '%'))" +
            " AND s.isDeleted =false")
            List<ShipmentMethod> search(@Param("keyword") String keyword, Pageable pageable);

    @Query("select count(s) from ShipmentMethod s where : keyword is null or" +
            " lower(s.name) like %:keyword% " +
            " AND s.isDeleted =false")
    int countSearch(String keyword);

}