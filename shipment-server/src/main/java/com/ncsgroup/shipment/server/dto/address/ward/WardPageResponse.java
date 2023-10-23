package com.ncsgroup.shipment.server.dto.address.ward;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class WardPageResponse {
  private List<WardResponse> wardResponses;
  private int count;
}
