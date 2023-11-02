package com.ncsgroup.shipment.server.controller.address;

import com.ncsgroup.shipment.server.dto.PageResponse;
import com.ncsgroup.shipment.server.dto.PageResponseGeneral;
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
import static com.ncsgroup.shipment.server.constanst.Constants.MessageCode.*;

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

  @GetMapping
  public PageResponseGeneral<PageResponse<AddressResponse>> list(
        @RequestParam(name = "keyword", required = false) String keyword,
        @RequestParam(name = "size", defaultValue = "10") int size,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "all", defaultValue = "false", required = false) boolean isAll,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    log.info("(list) keyword: {}, size : {}, page: {}, isAll: {}", keyword, size, page, isAll);
    return PageResponseGeneral.ofSuccess(messageService.getMessage(LIST_ADDRESS, language),
          addressService.list(keyword, size, page, isAll)
    );
  }

}
