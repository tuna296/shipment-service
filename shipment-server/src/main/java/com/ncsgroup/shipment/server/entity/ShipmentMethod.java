package com.ncsgroup.shipment.server.entity;

import com.ncsgroup.shipment.server.entity.base.BaseEntityWithUpdater;
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
    private String name;
    private String description;
    private double cost;
    private int estimatedDeliveryTime;
}
