package com.ncsgroup.shipment.server.entity.base;


import jakarta.persistence.MappedSuperclass;
import lombok.Data;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;


@Data
@MappedSuperclass
public abstract class BaseEntityWithUpdater extends BaseEntity {
  @LastModifiedBy
  private String lastUpdatedBy;
  @LastModifiedDate
  private Long lasUpdatedAt;

}
