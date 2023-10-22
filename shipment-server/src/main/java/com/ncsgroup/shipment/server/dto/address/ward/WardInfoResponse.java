package com.ncsgroup.shipment.server.dto.address.ward;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WardInfoResponse {
  private String wardName;
  private String wardNameEN;
  private String wardCodeName;
  private String code;
}
