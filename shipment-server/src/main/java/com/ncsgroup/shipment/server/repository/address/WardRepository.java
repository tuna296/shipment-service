package com.ncsgroup.shipment.server.repository.address;

import com.ncsgroup.shipment.server.dto.address.ward.WardInfoResponse;
import com.ncsgroup.shipment.server.dto.address.ward.WardResponse;
import com.ncsgroup.shipment.server.entity.address.Ward;
import com.ncsgroup.shipment.server.repository.BaseRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WardRepository extends BaseRepository<Ward> {
  boolean existsByCode(String code);

  @Query("SELECT new com.ncsgroup.shipment.server.dto.address.ward.WardInfoResponse " +
        "(w.name, w.nameEn, w.codeName, w.code) " +
        "FROM Ward w " +
        "WHERE w.code = :code")
  WardInfoResponse getByCode(String code);

  @Query("select new com.ncsgroup.shipment.server.dto.address.ward.WardResponse" +
        "(w.code, w.name, w.nameEn, w.fullName, w.fullNameEn, w.codeName) " +
        "FROM Ward w " +
        "WHERE (:districtCode is null or w.districtCode = :districtCode) " +
        "order by w.name")
  List<WardResponse> list(String districtCode);


  @Query("SELECT new com.ncsgroup.shipment.server.dto.address.ward.WardResponse " +
        "(w.code, w.name, w.nameEn, w.fullName, w.fullNameEn, w.codeName) " +
        "FROM Ward w " +
        "WHERE (:keyword is null or ( " +
        "lower( w.name) LIKE lower(concat('%', :keyword, '%')) OR " +
        "lower( w.nameEn) LIKE lower(concat('%', :keyword, '%')) OR " +
        "lower( w.fullName) LIKE lower(concat('%', :keyword, '%')) OR " +
        "lower( w.fullNameEn) LIKE lower(concat('%', :keyword, '%')) OR " +
        "lower( w.codeName) LIKE lower(concat('%', :keyword, '%')))) " +
        "and (:districtCode is null  or w.districtCode = :districtCode) " +
        "order by w.name")
  List<WardResponse> search(String keyword, String districtCode, Pageable pageable);

  @Query("SELECT count(DISTINCT w.code) " +
        "FROM Ward w " +
        "WHERE (:keyword is null or ( " +
        "lower(w.name) LIKE lower(concat('%', :keyword, '%')) OR " +
        "lower(w.nameEn) LIKE lower(concat('%', :keyword, '%')) OR " +
        "lower(w.fullName) LIKE lower(concat('%', :keyword, '%')) OR " +
        "lower(w.fullNameEn) LIKE lower(concat('%', :keyword, '%')) OR " +
        "lower(w.codeName) LIKE lower(concat('%', :keyword, '%')))) " +
        "and (:districtCode is null or w.districtCode = :districtCode)")
  int countSearch(String keyword, String districtCode);

  @Query("SELECT COUNT (DISTINCT w.code) " +
        "FROM Ward w " +
        "WHERE (:districtCode is null or w.districtCode = :districtCode)")
  int count(String districtCode);

}
