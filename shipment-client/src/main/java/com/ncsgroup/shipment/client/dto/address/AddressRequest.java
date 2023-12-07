package com.ncsgroup.shipment.client.dto.address;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import static com.ncsgroup.shipment.client.constants.Constants.Validate.*;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AddressRequest {
  @NotBlank(message = PROVINCE_BLANK)
  private String provinceCode;
  @NotBlank(message = DISTRICT_BLANK)
  private String districtCode;
  @NotBlank(message = WARD_BLANK)
  private String wardCode;
  @NotBlank(message = DETAIL_BLANK)
  private String detail;
  private String userId;
}

