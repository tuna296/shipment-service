package com.ncsgroup.shipment.server.exception.base;


import static com.ncsgroup.shipment.server.exception.base.StatusConstants.CONFLICT;

public class ConflictException extends BaseException {
  public ConflictException(String id, String objectName) {
    setStatus(CONFLICT);
    setCode("com.ncsgroup.profiling.exception.base.ConflictException");
    addParam("id", id);
    addParam("objectName", objectName);
  }

  public ConflictException(){
    setStatus(CONFLICT);
    setCode("com.ncsgroup.profiling.exception.base.ConflictException");
  }
}
