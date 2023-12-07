package com.ncsgroup.shipment.server.entity.address;

import com.ncsgroup.shipment.server.entity.base.BaseEntityWithUpdater;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;


@Entity
@Data
@Table(name = "addresses")
@NoArgsConstructor
@AllArgsConstructor
public class Address extends BaseEntityWithUpdater {
  @Column(name = "ward_code")
  private String wardCode;
  @Column(name = "district_code")
  private String districtCode;
  @Column(name = "province_code")
  private String provinceCode;
  @Column(name = "detail")
  private String detail;
  @Column(name = "is_deleted")
  private boolean isDeleted;
  @Column(name = "user_id")
  private String userId;

  public Address(String wardCode, String districtCode, String provinceCode, String detail) {
    this.wardCode = wardCode;
    this.districtCode = districtCode;
    this.provinceCode = provinceCode;
    this.detail = detail;
    this.isDeleted = false;
  }

  public Address(String wardCode, String districtCode, String provinceCode, String detail, String userId) {
    this.wardCode = wardCode;
    this.districtCode = districtCode;
    this.provinceCode = provinceCode;
    this.detail = detail;
    this.isDeleted = false;
    this.userId = userId;
  }
}