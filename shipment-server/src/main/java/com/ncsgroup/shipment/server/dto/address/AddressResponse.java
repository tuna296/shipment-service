package com.ncsgroup.shipment.server.dto.address;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
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
  private String id;
  private String wards;
  private String districts;
  private String provinces;
  private String detail;

  public AddressResponse(String wards, String districts, String provinces, String detail) {
    this.wards = wards;
    this.districts = districts;
    this.provinces = provinces;
    this.detail = detail;
  }
}
