package com.ncsgroup.shipment.server.controller.address;

import com.ncsgroup.shipment.server.dto.ResponseGeneral;
import com.ncsgroup.shipment.server.dto.address.district.DistrictInfoResponse;
import com.ncsgroup.shipment.server.dto.address.district.DistrictPageResponse;
import com.ncsgroup.shipment.server.dto.address.province.ProvinceInfoResponse;
import com.ncsgroup.shipment.server.service.MessageService;
import com.ncsgroup.shipment.server.service.address.DistrictService;
import dto.address.SearchDistrictRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import static com.ncsgroup.shipment.server.constanst.Constants.CommonConstants.DEFAULT_LANGUAGE;
import static com.ncsgroup.shipment.server.constanst.Constants.CommonConstants.LANGUAGE;
import static com.ncsgroup.shipment.server.constanst.Constants.MessageCode.*;

@Slf4j
@RestController
@RequestMapping("/api/v1/districts")
@RequiredArgsConstructor
public class DistrictController {
  private final MessageService messageService;
  private final DistrictService districtService;

  @PostMapping
  public ResponseGeneral<DistrictPageResponse> list(
        @RequestBody(required = false) SearchDistrictRequest request,
        @RequestParam(name = "size", defaultValue = "10") int size,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "all", defaultValue = "false") boolean isAll,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    log.info("(search) request: {}, size:{}, page:{}, isAll:{}", request, size, page, isAll);

    return ResponseGeneral.ofSuccess(
          messageService.getMessage(GET_DISTRICT_SUCCESS, language),
          districtService.search(request, size, page, isAll)
    );
  }

  @GetMapping("details/{code}")
  public ResponseGeneral<DistrictInfoResponse> detail(
        @PathVariable String code,
        @RequestHeader(name = LANGUAGE, defaultValue = DEFAULT_LANGUAGE) String language
  ) {
    return ResponseGeneral.ofSuccess(
          messageService.getMessage(DETAIL_DISTRICT, language),
          districtService.detail(code)
    );
  }
}