package com.ncsgroup.shipment.server.repository.address;

import com.ncsgroup.shipment.server.dto.address.AddressResponse;
import com.ncsgroup.shipment.server.entity.address.Address;
import com.ncsgroup.shipment.server.repository.BaseRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface AddressRepository extends BaseRepository<Address> {
  @Query("""
            SELECT new com.ncsgroup.shipment.server.dto.address.AddressResponse
            (a.id, w.nameEn,d.nameEn,p.nameEn, a.detail)
            FROM Address a
            LEFT JOIN Province p ON a.provinceCode = p.code
            LEFT JOIN District d ON a.districtCode = d.code
            LEFT JOIN Ward w ON a.wardCode = w.code
            WHERE (:keyword is null or
            (lower(p.nameEn) LIKE lower(concat('%', :keyword, '%')) OR
            lower(d.nameEn) LIKE lower(concat('%', :keyword, '%')) OR
            lower(w.nameEn) LIKE lower(concat('%', :keyword, '%')) OR
            lower(a.detail) LIKE lower(concat('%', :keyword, '%'))) )
            AND a.isDeleted = false
            ORDER BY a.provinceCode
        """)
  Page<AddressResponse> search(Pageable pageable, String keyword);

  @Query("""
            SELECT new com.ncsgroup.shipment.server.dto.address.AddressResponse
            (a.id, w.nameEn,d.nameEn,p.nameEn, a.detail)
            FROM Address a
            LEFT JOIN Province p ON a.provinceCode = p.code
            LEFT JOIN District d ON a.districtCode = d.code
            LEFT JOIN Ward w ON a.wardCode = w.code
            WHERE a.isDeleted=false
        """)
  Page<AddressResponse> findAllAddress(Pageable pageable);

  @Query("SELECT EXISTS(SELECT true FROM Address a WHERE a.id = :id AND a.isDeleted = false)")
  boolean existsById(@Param("id") String id);

  @Query("""
            SELECT new com.ncsgroup.shipment.server.dto.address.AddressResponse
            (a.id, w.nameEn,d.nameEn,p.nameEn, a.detail)
            FROM Address a
            LEFT JOIN Province p ON a.provinceCode = p.code
            LEFT JOIN District d ON a.districtCode = d.code
            LEFT JOIN Ward w ON a.wardCode = w.code
            WHERE a.id = :id AND a.isDeleted=false
        """)
  AddressResponse findAddressById(@Param("id") String id);

  @Modifying
  @Transactional
  @Query("UPDATE Address a SET a.isDeleted = true WHERE a.id = :id")
  void deleteById(String id);
}
