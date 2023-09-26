package com.ncsgroup.shipment.server.dto.response;


import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import com.ncsgroup.shipment.server.utils.DateUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class ResponseGeneral<T> {
  private int status;
  private String message;
  private T data;
  private String timestamp;

  public static <T> ResponseGeneral<T> of(int status, String message, T data) {
    return of(status, message, data, DateUtils.getCurrentDateString());
  }
}
