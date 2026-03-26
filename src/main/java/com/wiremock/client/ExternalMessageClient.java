package com.wiremock.client;

import com.wiremock.dto.ExternalMessageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@FeignClient(
        name = "externalMessageClient",
        url = "${external.api.base-url}"
)
public interface ExternalMessageClient {

    @GetMapping("/external/message")
    ExternalMessageResponse getMessage(
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "lang", required = false) String lang
    );

    @GetMapping("/external/header-message")
    ExternalMessageResponse getHeaderMessage(
            @RequestHeader(value = "Authorization", required = false) String authorization,
            @RequestHeader(value = "X-Client-Id", required = false) String xClientId
    );

    @GetMapping("/external/cookie-message")
    ExternalMessageResponse getCookieMessage(
            @CookieValue(value = "SESSION", required = false) String session,
            @CookieValue(value = "USER_ID", required = false) String userId
    );

    @GetMapping("/external/json")
    ExternalMessageResponse sendJson(
            Map<String, Object> data
    );

    @GetMapping("/external/header-response")
    ResponseEntity<ExternalMessageResponse> headerResponse();

}
