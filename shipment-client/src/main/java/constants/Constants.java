package constants;

public class Constants {
  public static class Validate {
    public static final String NAME_BLANK = "com.ncsgroup.shipment.client.dto.name.NotNull";
    public static final String PRICE_BLANK = "com.ncsgroup.shipment.client.dto.pricePerKilometer.NotNull";
    public static final String DESCRIPTION_BLANK = "com.ncsgroup.shipment.client.dto.description.NotNull";
    public static final String PROVINCE_BLANK = "com.ncsgroup.shipment.client.dto.address.AddressRequest.provinceCode.NotNull";
    public static final String DISTRICT_BLANK = "com.ncsgroup.shipment.client.dto.address.AddressRequest.districtCode.NotNull";
    public static final String WARD_BLANK = "com.ncsgroup.shipment.client.dto.address.AddressRequest.wardCode.NotNull";
    public static final String DETAIL_BLANK = "com.ncsgroup.shipment.client.dto.address.AddressRequest.detail.NotNull";
    public static final String ADDRESS_NOT_BLANK="com.ncsgroup.shipment.client.dto.ShipmentRequest.Address.NotBank";
    public static final String PRICE_SHIPMENT_BLANK="com.ncsgroup.shipment.client.dto.ShipmentRequest.PriceShipment.NotBlank";
    public static final String SHIPMENT_METHOD_BLANK="com.ncsgroup.shipment.client.dto.ShipmentRequest.ShipmentMethod.NotBlank";
  }
}
