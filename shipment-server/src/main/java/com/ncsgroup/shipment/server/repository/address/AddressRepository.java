package com.ncsgroup.shipment.server.repository.address;

import com.ncsgroup.shipment.server.dto.address.AddressResponse;
import com.ncsgroup.shipment.server.entity.address.Address;
import com.ncsgroup.shipment.server.repository.BaseRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AddressRepository extends BaseRepository<Address> {
  @Query(value = "SELECT new com.ncsgroup.shipment.server.dto.address.AddressResponse" +
        "(a.wardCode, a.districtCode, a.provinceCode, a.detail) " +
        "FROM Address a " +
        "WHERE (:keyword is null or " +
        "(lower(a.wardCode) LIKE lower(concat('%', :keyword, '%')) OR " +
        "lower(a.districtCode) LIKE lower(concat('%', :keyword, '%')) OR " +
        "lower(a.provinceCode) LIKE lower(concat('%', :keyword, '%')) OR " +
        "lower(a.detail) LIKE lower(concat('%', :keyword, '%'))) ) " +
        "AND a.isDeleted = false " +
        "ORDER BY a.provinceCode")
  List<AddressResponse> search(PageRequest pageRequest, String keyword);

  @Query("SELECT COUNT(a) FROM Address a WHERE (:keyword is null or " +
        "(lower(a.wardCode) LIKE lower(concat('%', :keyword, '%')) OR " +
        "lower(a.districtCode) LIKE lower(concat('%', :keyword, '%')) OR " +
        "lower(a.provinceCode) LIKE lower(concat('%', :keyword, '%')) OR " +
        "lower(a.detail) LIKE lower(concat('%', :keyword, '%'))) ) " +
        "AND a.isDeleted = false ")
  int countSearch(String keyword);

  @Query("select count(a) from Address a ")
  int countFindAllAddress();

  @Query("select a from Address a where a.isDeleted=false ")
  List<AddressResponse> findAllAddress();
}
