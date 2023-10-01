package com.ncsgroup.shipment.server.dto.shipmentmethod;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShipmentMethodResponse {
    private String name;
    private String description;
    private double pricePerKilometer;
    public static ShipmentMethodResponse from(
            String name,
            String description,
            double pricePerKilometer
    ){
        return new ShipmentMethodResponse(name, description, pricePerKilometer);
    }
}
