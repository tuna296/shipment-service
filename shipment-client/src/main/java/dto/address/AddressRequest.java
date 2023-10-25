package dto.address;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Data;

@Data
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AddressRequest {
  private String provinceCode;
  private String districtCode;
  private String wardCode;
  private String detail;
}

