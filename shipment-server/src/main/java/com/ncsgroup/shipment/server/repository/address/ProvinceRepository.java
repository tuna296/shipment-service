package com.ncsgroup.shipment.server.repository.address;

import com.ncsgroup.shipment.server.dto.address.province.ProvinceInfoResponse;
import com.ncsgroup.shipment.server.entity.address.Province;
import com.ncsgroup.shipment.server.repository.BaseRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProvinceRepository extends BaseRepository<Province> {
  @Query("select new com.ncsgroup.shipment.server.dto.address.province.ProvinceInfoResponse" +
        "(p.name, p.nameEn, p.codeName, p.code) " +
        "from Province p " +
        "where p.code = :code")
  ProvinceInfoResponse getByCode(@Param("code") String code);

  boolean existsByCode(String code);

  @Query("SELECT p FROM Province p WHERE :keyword is null " +
        "or lower(p.code) LIKE lower(concat('%', :keyword, '%'))" +
        "or lower(p.name) LIKE lower(concat('%', :keyword, '%'))" +
        "or lower(p.nameEn) LIKE lower(concat('%', :keyword, '%'))" +
        "or lower(p.fullName) LIKE lower(concat('%', :keyword, '%'))" +
        "or lower(p.fullNameEn) LIKE lower(concat('%', :keyword, '%'))" +
        "or lower(p.codeName) LIKE lower(concat('%', :keyword, '%'))")
  List<Province> search(@Param("keyword") String keyword, Pageable pageable);

  @Query("SELECT count(DISTINCT p.code) " +
        "FROM Province p " +
        "WHERE :keyword is null " +
        "or lower(p.name) LIKE lower(concat('%', :keyword, '%'))" +
        "or lower(p.nameEn) LIKE lower(concat('%', :keyword, '%'))" +
        "or lower(p.fullName) LIKE lower(concat('%', :keyword, '%'))" +
        "or lower(p.fullNameEn) LIKE lower(concat('%', :keyword, '%'))" +
        "or lower(p.codeName) LIKE lower(concat('%', :keyword, '%'))")
  int countSearch(String keyword);

}
