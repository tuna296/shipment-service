package com.ncsgroup.shipment.server.controller;

import com.ncsgroup.shipment.server.dto.ResponseGeneral;
import com.ncsgroup.shipment.server.dto.shipment.ShipmentResponse;
import com.ncsgroup.shipment.server.facade.ShipmentFacadeService;
import com.ncsgroup.shipment.server.service.MessageService;
import dto.ShipmentRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.ncsgroup.shipment.server.constanst.Constants.CommonConstants.DEFAULT_LANGUAGE;
import static com.ncsgroup.shipment.server.constanst.Constants.CommonConstants.LANGUAGE;
import static com.ncsgroup.shipment.server.constanst.Constants.MessageCode.CREATE_SHIPMENT_SUCCESS;
import static com.ncsgroup.shipment.server.constanst.Constants.MessageCode.UPDATE_SHIPMENT_SUCCESS;

@RestController
@RequestMapping("/api/v1/shipments")
@Slf4j
@RequiredArgsConstructor

public class ShipmentController {
  private final ShipmentFacadeService shipmentFacadeService;
  private final MessageService messageService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public ResponseGeneral<ShipmentResponse> create(
        @RequestBody @Validated ShipmentRequest request,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    log.info("(create)request: {}", request);

    return ResponseGeneral.ofCreated(
          messageService.getMessage(CREATE_SHIPMENT_SUCCESS, language),
          shipmentFacadeService.create(request));
  }

  @PutMapping("{id}")
  public ResponseGeneral<ShipmentResponse> update(
        @RequestBody @Validated ShipmentRequest request,
        @PathVariable String id,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    log.info("(update)request:{}, id: {}", request, id);

    return ResponseGeneral.ofSuccess(
          messageService.getMessage(UPDATE_SHIPMENT_SUCCESS, language),
          shipmentFacadeService.update(request, id));
  }

}
