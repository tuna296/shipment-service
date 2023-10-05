package com.ncsgroup.shipment.server.exception.base;


public class NotFoundException extends BaseException {
    public NotFoundException(String id, String objectName) {
        setCode("com.ncsgroup.profiling.exception.base.NotFoundException");
        setStatus(StatusConstants.NOT_FOUND);
        addParam("id", id);
        addParam("objectName", objectName);
    }

    public NotFoundException() {
        setCode("com.ncsgroup.profiling.exception.base.NotFoundException");
        setStatus(StatusConstants.NOT_FOUND);
    }
}
