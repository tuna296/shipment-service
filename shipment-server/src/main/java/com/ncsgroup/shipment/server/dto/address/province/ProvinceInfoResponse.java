package com.ncsgroup.shipment.server.dto.address.province;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProvinceInfoResponse {
  private String provinceName;
  private String provinceNameEn;
  private String provinceCodeName;
  private String code;
}