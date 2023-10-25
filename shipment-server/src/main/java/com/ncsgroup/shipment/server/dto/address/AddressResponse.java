package com.ncsgroup.shipment.server.dto.address;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.ncsgroup.shipment.server.dto.address.district.DistrictInfoResponse;
import com.ncsgroup.shipment.server.dto.address.province.ProvinceInfoResponse;
import com.ncsgroup.shipment.server.dto.address.ward.WardInfoResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AddressResponse {
  private String  id;
  private WardInfoResponse wards;
  private DistrictInfoResponse districts;
  private ProvinceInfoResponse provinces;
  private String detail;
}
