package com.ncsgroup.shipment.server.entity;

import com.ncsgroup.shipment.server.entity.base.BaseEntityWithUpdater;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "shipment_method")
@AllArgsConstructor(staticName = "of")
@NoArgsConstructor
public class ShipmentMethod extends BaseEntityWithUpdater {
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @Column(name = "pricePerKilometer")
    private double pricePerKilometer;

    public static ShipmentMethod from(
            String name,
            String description,
            double pricePerKilometer
    ) {
        return ShipmentMethod.of(name, description, pricePerKilometer);
    }
}
