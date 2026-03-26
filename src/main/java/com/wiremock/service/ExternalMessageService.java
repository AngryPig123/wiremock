package com.wiremock.service;

import com.wiremock.client.ExternalMessageClient;
import com.wiremock.dto.ExternalMessageResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class ExternalMessageService {

    private final ExternalMessageClient externalMessageClient;

    public ExternalMessageService(ExternalMessageClient externalMessageClient) {
        this.externalMessageClient = externalMessageClient;
    }

    public String getMessage() {
        return externalMessageClient.getMessage(null, null).message();
    }

    public String getMessageByTypeAndLang(String type, String lang) {
        return externalMessageClient.getMessage(type, lang).message();
    }

    public String getHeaderMessage(String authorization, String xClientId) {
        return externalMessageClient.getHeaderMessage(authorization, xClientId).message();
    }

    public String getCookieMessage(String session, String userId) {
        return externalMessageClient.getCookieMessage(session, userId).message();
    }

    public String sendJson(Map<String, Object> data) {
        return externalMessageClient.sendJson(data).message();
    }

    public ResponseEntity<ExternalMessageResponse> headerResponse() {
        return externalMessageClient.headerResponse();
    }

}
