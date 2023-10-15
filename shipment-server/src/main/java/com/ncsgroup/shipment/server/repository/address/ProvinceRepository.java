package com.ncsgroup.shipment.server.repository.address;

import com.ncsgroup.shipment.server.dto.address.province.ProvinceResponse;
import com.ncsgroup.shipment.server.entity.address.Province;
import com.ncsgroup.shipment.server.repository.BaseRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProvinceRepository extends BaseRepository<Province> {
  @Query("select p from Province p where p.code =: code")
  ProvinceResponse getByCode(String code);

  boolean existsByCode(String code);

  @Query("SELECT p " +
        "FROM Province p " +
        "WHERE :keyword is null or ( " +
        "lower( p.name) LIKE :keyword% OR " +
        "lower( p.nameEn) LIKE :keyword% OR " +
        "lower( p.fullName) LIKE :keyword% OR " +
        "lower( p.fullNameEn) LIKE :keyword% OR " +
        "lower( p.codeName) LIKE :keyword%) " +
        "ORDER BY p.name")
  List<Province> search(@Param("keyword") String keyword, Pageable pageable);

  @Query("SELECT (p.code, p.name, p.nameEn, p.fullName, p.fullNameEn, p.codeName) " +
        "FROM Province p " +
        "ORDER BY p.name")
  List<ProvinceResponse> list();
  @Query("SELECT count(DISTINCT p.code) " +
        "FROM Province p " +
        "WHERE :keyword is null or ( " +
        "lower( p.name) LIKE :keyword% OR " +
        "lower( p.nameEn) LIKE :keyword% OR " +
        "lower( p.fullName)  LIKE :keyword% OR " +
        "lower( p.fullNameEn) LIKE :keyword% OR " +
        "lower( p.codeName) LIKE :keyword%)")
  int countSearch(String keyword);
}
