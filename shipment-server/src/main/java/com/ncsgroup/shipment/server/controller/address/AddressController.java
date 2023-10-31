package com.ncsgroup.shipment.server.controller.address;

import com.ncsgroup.shipment.server.dto.ResponseGeneral;
import com.ncsgroup.shipment.server.dto.address.AddressResponse;
import com.ncsgroup.shipment.server.facade.AddressFacadeService;
import com.ncsgroup.shipment.server.service.MessageService;
import com.ncsgroup.shipment.server.service.address.AddressService;
import dto.address.AddressRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.ncsgroup.shipment.server.constanst.Constants.CommonConstants.DEFAULT_LANGUAGE;
import static com.ncsgroup.shipment.server.constanst.Constants.CommonConstants.LANGUAGE;
import static com.ncsgroup.shipment.server.constanst.Constants.MessageCode.CREATE_ADDRESS_SUCCESS;
import static com.ncsgroup.shipment.server.constanst.Constants.MessageCode.DETAIL_ADDRESS;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/addresses")
@Slf4j
public class AddressController {
  private final AddressService addressService;
  private final AddressFacadeService addressFacadeService;
  private final MessageService messageService;

  @ResponseStatus(HttpStatus.CREATED)
  @PostMapping
  public ResponseGeneral<AddressResponse> create(
        @RequestBody AddressRequest request,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    log.info("(create)request: {}", request);

    return ResponseGeneral.ofCreated(
          messageService.getMessage(CREATE_ADDRESS_SUCCESS, language),
          addressFacadeService.createAddress(request)
    );
  }

  @GetMapping("{id}")
  public ResponseGeneral<AddressResponse> detail(
        @PathVariable String id,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    log.info("(detail) id: {}", id);

    return ResponseGeneral.ofSuccess(
          messageService.getMessage(DETAIL_ADDRESS, language),
          addressService.detail(id)
    );
  }

}
