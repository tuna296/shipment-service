package com.ncsgroup.shipment.server.utils;

import org.modelmapper.ModelMapper;

public class MapperUtils {
  private MapperUtils() {
  }

  public static final ModelMapper MODEL_MAPPER = new ModelMapper();

  public static <T, R> R toDTO(T entity, Class<R> dtoClass) {
    return MODEL_MAPPER.map(entity, dtoClass);
  }

  public static <T, R> R toEntity(T dto, Class<R> entityClass) {
    return MODEL_MAPPER.map(dto, entityClass);
  }

}
