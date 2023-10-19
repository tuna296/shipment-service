package com.ncsgroup.shipment.server.dto.address.district;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DistrictResponse {
  private String code;
  private String name;
  private String nameEn;
  private String fullName;
  private String fullNameEn;
  private String codeName;

  public DistrictResponse(String code, String name, String nameEn, String fullName, String fullNameEn, String codeName) {
    this.code = code;
    this.name = name;
    this.nameEn = nameEn;
    this.fullName = fullName;
    this.fullNameEn = fullNameEn;
    this.codeName = codeName;
  }
}