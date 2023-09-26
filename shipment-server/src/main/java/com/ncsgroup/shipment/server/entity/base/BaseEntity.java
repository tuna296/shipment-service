package com.ncsgroup.shipment.server.entity.base;


import io.micrometer.common.util.StringUtils;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.UUID;

@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {
  @Id
  private String id;

  @CreatedBy
  private String createBy;

  @CreatedDate
  private Long createAt;

  @PrePersist
  public void ensureId() {
    this.id = StringUtils.isBlank(this.id) ? UUID.randomUUID().toString() : this.id;
  }

}

