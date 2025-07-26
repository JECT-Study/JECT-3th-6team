package com.example.demo.presentation.controller.handler;

import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class OAuth2FailureHandler {

    @Value("${custom.app.frontend-url}")
    private String frontendUrl;

    public void handleFailure(HttpServletResponse response, Exception e) throws IOException {
        String reasonCode = resolveReasonCode(e);
        redirectToFail(response, reasonCode);
    }

    private String resolveReasonCode(Exception e) {
        if (e instanceof HttpClientErrorException || e instanceof IllegalStateException) {
            return "token_exchange_failed";
        }
        return "server_error";
    }

    private void redirectToFail(HttpServletResponse response, String reasonCode) throws IOException {
        String redirectUrl = UriComponentsBuilder.fromUriString(frontendUrl)
            .path("/login/fail")
            .queryParam("reason", reasonCode)
            .build()
            .toUriString();
        response.sendRedirect(redirectUrl);
    }
}