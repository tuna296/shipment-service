package com.ncsgroup.shipment.server.dto.address.district;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(staticName = "of")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DistrictInfoResponse {
  private String districtName;
  private String districtNameEn;
  private String districtCodeName;
  private String code;

  public DistrictInfoResponse(String districtName, String districtNameEn, String districtCodeName, String code) {
    this.districtName = districtName;
    this.districtNameEn = districtNameEn;
    this.districtCodeName = districtCodeName;
    this.code = code;
  }
}
