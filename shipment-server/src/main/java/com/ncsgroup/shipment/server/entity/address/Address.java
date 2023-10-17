package com.ncsgroup.shipment.server.entity.address;

import com.ncsgroup.shipment.server.entity.base.BaseEntityWithUpdater;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;


@Entity
@Data
@Table(name = "address")
@NoArgsConstructor
@AllArgsConstructor
public class Address extends BaseEntityWithUpdater {
    private String wardCode;
    private String districtCode;
    private String provinceCode;
    private String detail;
}