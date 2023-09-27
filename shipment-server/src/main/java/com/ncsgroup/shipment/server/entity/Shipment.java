package com.ncsgroup.shipment.server.entity;

import com.ncsgroup.shipment.server.entity.base.BaseEntityWithUpdater;
import com.ncsgroup.shipment.server.entity.enums.Status;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "shipments")
@AllArgsConstructor
@NoArgsConstructor
public class Shipment extends BaseEntityWithUpdater {
    private String code;
    private String fromAddressId;
    private String toAddressId;
    private double price;
    private String method;
    private long shipmentDate;
    private long expectedDeliveryDate;
    @Enumerated(EnumType.STRING)
    private Status status;

}
