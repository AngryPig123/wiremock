package com.wiremock;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.wiremock.dto.ExternalMessageResponse;
import com.wiremock.service.ExternalMessageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;

import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureWireMock(port = 0)
@TestPropertySource(properties = {
        "external.api.base-url=http://localhost:${wiremock.server.port}"
})
class ExternalMessageServiceTest {

    @Autowired
    private ExternalMessageService externalMessageService;

    @Test
    void returnsMessageFromWireMockServer() {
        WireMock.stubFor(get(urlEqualTo("/external/message"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                {
                                  "message": "wiremock test success"
                                }
                                """)));

        String message = externalMessageService.getMessage();

        assertThat(message).isEqualTo("wiremock test success");
        WireMock.verify(getRequestedFor(urlEqualTo("/X")));
    }

    @Test
    void returnsMessageWithQueryParam() {
        WireMock.stubFor(get(urlPathEqualTo("/external/message"))
                .withQueryParam("type", equalTo("notice"))
                .withQueryParam("lang", equalTo("ko"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                {
                                  "message": "query param success"
                                }
                                """)));

        String message = externalMessageService.getMessageByTypeAndLang("notice", "ko");

        assertThat(message).isEqualTo("query param success");

        WireMock.verify(getRequestedFor(urlPathEqualTo("/external/message"))
                .withQueryParam("type", equalTo("notice"))
                .withQueryParam("lang", equalTo("ko")));
    }


    @Test
    void returnsMessageWhenHeaderMatches() {
        WireMock.stubFor(get(urlEqualTo("/external/header-message"))
                .withHeader("Authorization", equalTo("Bearer test-token"))
                .withHeader("X-Client-Id", equalTo("client-001"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                {
                                  "message": "header success"
                                }
                                """)));

        String message = externalMessageService.getHeaderMessage("Bearer test-token", "client-001");

        assertThat(message).isEqualTo("header success");

        WireMock.verify(getRequestedFor(urlEqualTo("/external/header-message"))
                .withHeader("Authorization", equalTo("Bearer test-token"))
                .withHeader("X-Client-Id", equalTo("client-001")));
    }

    @Test
    void returnsMessageWhenCookieMatches() {
        WireMock.stubFor(get(urlEqualTo("/external/cookie-message"))
                .withCookie("SESSION", equalTo("session-123"))
                .withCookie("USER_ID", equalTo("angrypig"))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                {
                                  "message": "cookie success"
                                }
                                """)));

        String message = externalMessageService.getCookieMessage("session-123", "angrypig");

        assertThat(message).isEqualTo("cookie success");

        WireMock.verify(getRequestedFor(urlEqualTo("/external/cookie-message"))
                .withCookie("SESSION", equalTo("session-123"))
                .withCookie("USER_ID", equalTo("angrypig")));
    }

    @Test
    void returnsMessageWhenJsonBodyMatches() {
        WireMock.stubFor(post(urlEqualTo("/external/json"))
                .withHeader("Content-Type", containing("application/json"))
                .withRequestBody(equalToJson("""
                        {
                          "name": "angrypig",
                          "age": 30
                        }
                        """))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                {
                                  "message": "json body success"
                                }
                                """)));

        String message = externalMessageService.sendJson(Map.of("name", "angrypig", "age", 30));

        assertThat(message).isEqualTo("json body success");

        WireMock.verify(postRequestedFor(urlEqualTo("/external/json"))
                .withRequestBody(equalToJson("""
                        {
                          "name": "angrypig",
                          "age": 30
                        }
                        """)));
    }

    @Test
    void returnsMessageWhenJsonPathMatches() {
        WireMock.stubFor(post(urlEqualTo("/external/json"))
                .withHeader("Content-Type", containing("application/json"))
                .withRequestBody(matchingJsonPath("$.user.id", equalTo("u-100")))
                .withRequestBody(matchingJsonPath("$.user.name", equalTo("angrypig")))
                .willReturn(aResponse()
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                {
                                  "message": "json path success"
                                }
                                """)));


        String message = externalMessageService.sendJson(Map.of(
                "user", Map.of("id", "u-100", "name", "angrypig")
        ));

        assertThat(message).isEqualTo("json path success");

        WireMock.verify(postRequestedFor(urlEqualTo("/external/json"))
                .withRequestBody(matchingJsonPath("$.user.id", equalTo("u-100")))
                .withRequestBody(matchingJsonPath("$.user.name", equalTo("angrypig"))));
    }

    @Test
    void returnsResponseHeader() {
        WireMock.stubFor(get(urlEqualTo("/external/header-response"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("X-Trace-Id", "trace-123")
                        .withHeader("Content-Type", "application/json")
                        .withBody("""
                                {
                                  "message": "ok"
                                }
                                """)));

        ResponseEntity<ExternalMessageResponse> responseEntity = externalMessageService.headerResponse();

        assertThat(responseEntity.getHeaders().get("X-Trace-Id").get(0)).isEqualTo("trace-123");

        WireMock.verify(getRequestedFor(urlEqualTo("/external/header-response")));
    }

}
