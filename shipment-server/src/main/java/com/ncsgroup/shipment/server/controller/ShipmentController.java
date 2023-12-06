package com.ncsgroup.shipment.server.controller;

import com.ncsgroup.shipment.server.dto.PageResponse;
import com.ncsgroup.shipment.server.dto.ResponseGeneral;
import com.ncsgroup.shipment.server.dto.shipment.ShipmentResponse;
import com.ncsgroup.shipment.server.facade.ShipmentFacadeService;
import com.ncsgroup.shipment.server.service.MessageService;
import com.ncsgroup.shipment.client.dto.ShipmentRequest;
import com.ncsgroup.shipment.server.service.ShipmentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import static com.ncsgroup.shipment.server.constanst.Constants.CommonConstants.DEFAULT_LANGUAGE;
import static com.ncsgroup.shipment.server.constanst.Constants.CommonConstants.LANGUAGE;
import static com.ncsgroup.shipment.server.constanst.Constants.MessageCode.*;

@RestController
@RequestMapping("/api/v1/shipments")
@Slf4j
@RequiredArgsConstructor

public class ShipmentController {
  private final ShipmentFacadeService shipmentFacadeService;
  private final MessageService messageService;
  private final ShipmentService shipmentService;

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

  @DeleteMapping("{id}")
  public ResponseGeneral<Void> delete(
        @PathVariable String id,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    log.info("(delete) id: {}", id);
    shipmentService.delete(id);
    return ResponseGeneral.ofSuccess(
          messageService.getMessage(DELETE_SHIPMENT_SUCCESS, language)
    );
  }

  @GetMapping("{id}")
  public ResponseGeneral<ShipmentResponse> detail(
        @PathVariable String id,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    log.info("(detail) id: {}", id);

    return ResponseGeneral.ofSuccess(
          messageService.getMessage(DETAIL_SHIPMENT_SUCCESS, language),
          shipmentFacadeService.detail(id));
  }

  @GetMapping
  public ResponseGeneral<PageResponse<ShipmentResponse>> list(
        @RequestParam(name = "keyword", required = false) String keyword,
        @RequestParam(name = "size", defaultValue = "10") int size,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "all", defaultValue = "false", required = false) boolean isAll,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    log.info("(list) keyword: {}, size : {}, page: {}, isAll: {}", keyword, size, page, isAll);
    return ResponseGeneral.ofSuccess(messageService.getMessage(LIST_SHIPMENT_SUCCESS, language),
          shipmentService.list(keyword, size, page, isAll)
    );
  }

}
