package com.ncsgroup.shipment.server.repository.address;

import com.ncsgroup.shipment.server.dto.address.district.DistrictInfoResponse;
import com.ncsgroup.shipment.server.dto.address.district.DistrictResponse;
import com.ncsgroup.shipment.server.entity.address.District;
import com.ncsgroup.shipment.server.repository.BaseRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DistrictRepository extends BaseRepository<District> {
  boolean existsByCode(String code);

  @Query("SELECT new com.ncsgroup.shipment.server.dto.address.district.DistrictInfoResponse " +
        "(d.name, d.nameEn, d.codeName, d.code) " +
        "FROM District d " +
        "WHERE d.code = :code")
  DistrictInfoResponse getByCode(String code);

  @Query("select new com.ncsgroup.shipment.server.dto.address.district.DistrictResponse" +
        "(d.code, d.name, d.nameEn, d.fullName, d.fullNameEn, d.codeName) " +
        "FROM District d " +
        "WHERE (:provinceCode is null or d.provinceCode = :provinceCode) " +
        "order by d.name")
  List<DistrictResponse> list(String provinceCode);


  @Query("SELECT new com.ncsgroup.shipment.server.dto.address.district.DistrictResponse " +
        "(d.code, d.name, d.nameEn, d.fullName, d.fullNameEn, d.codeName) " +
        "FROM District d " +
        "WHERE (:keyword is null or ( " +
        "lower( d.name) LIKE lower(concat('%', :keyword, '%')) OR " +
        "lower( d.nameEn) LIKE lower(concat('%', :keyword, '%')) OR " +
        "lower( d.fullName) LIKE lower(concat('%', :keyword, '%')) OR " +
        "lower( d.fullNameEn) LIKE lower(concat('%', :keyword, '%')) OR " +
        "lower( d.codeName) LIKE lower(concat('%', :keyword, '%')))) " +
        "and (:provinceCode is null  or d.provinceCode = :provinceCode) " +
        "order by d.name")
  List<DistrictResponse> search(String keyword, String provinceCode, Pageable pageable);

  @Query("SELECT count(DISTINCT d.code) " +
        "FROM District d " +
        "WHERE (:keyword is null or ( " +
        "lower(d.name) LIKE lower(concat('%', :keyword, '%')) OR " +
        "lower(d.nameEn) LIKE lower(concat('%', :keyword, '%')) OR " +
        "lower(d.fullName) LIKE lower(concat('%', :keyword, '%')) OR " +
        "lower(d.fullNameEn) LIKE lower(concat('%', :keyword, '%')) OR " +
        "lower(d.codeName) LIKE lower(concat('%', :keyword, '%')))) " +
        "and (:provinceCode is null or d.provinceCode = :provinceCode)")
  int countSearch(String keyword, String provinceCode);

  @Query("SELECT COUNT (DISTINCT d.code) " +
        "FROM District d " +
        "WHERE (:provinceCode is null or d.provinceCode = :provinceCode)")
  int count(String provinceCode);
}
