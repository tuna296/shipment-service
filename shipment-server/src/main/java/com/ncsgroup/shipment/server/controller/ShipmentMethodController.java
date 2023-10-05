package com.ncsgroup.shipment.server.controller;

import com.ncsgroup.shipment.server.dto.response.ResponseGeneral;
import com.ncsgroup.shipment.server.dto.shipmentmethod.ShipmentMethodResponse;
import com.ncsgroup.shipment.server.service.MessageService;
import com.ncsgroup.shipment.server.service.ShipmentMethodService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

import dto.ShipmentMethodRequest;

import static com.ncsgroup.shipment.server.constanst.ProfilingConstants.CommonConstants.DEFAULT_LANGUAGE;
import static com.ncsgroup.shipment.server.constanst.ProfilingConstants.CommonConstants.LANGUAGE;
import static com.ncsgroup.shipment.server.constanst.ProfilingConstants.MessageCode.CREATE_SHIPMENT_METHOD_SUCCESS;


@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/shipment-methods")
@Slf4j
public class ShipmentMethodController {
    private final ShipmentMethodService shipmentMethodService;
    private final MessageService messageService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)

    public ResponseGeneral<ShipmentMethodResponse> create(
            @Valid @RequestBody ShipmentMethodRequest request,
            @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
    ) {
        return ResponseGeneral.ofCreated(
                messageService.getMessage(CREATE_SHIPMENT_METHOD_SUCCESS, language),
                shipmentMethodService.create(request)
        );
    }
}
