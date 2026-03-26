package com.wiremock.controller;

import com.wiremock.service.ExternalMessageService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageController {

    private final ExternalMessageService externalMessageService;

    public MessageController(ExternalMessageService externalMessageService) {
        this.externalMessageService = externalMessageService;
    }

//    @GetMapping("/messages")
//    public String getMessage() {
//        return externalMessageService.getMessage();
//    }

}
