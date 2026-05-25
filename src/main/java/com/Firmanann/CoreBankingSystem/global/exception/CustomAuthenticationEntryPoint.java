package com.Firmanann.CoreBankingSystem.global.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {

        // 1. Set response header ke JSON dan HTTP Status 401 Unauthorized
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // 2. Desain format JSON (sesuai dengan GlobalExceptionHandler)
        Map<String, Object> responseBody = new LinkedHashMap<>();
        responseBody.put("status", "error");
        responseBody.put("message", ErrorCode.UNAUTHORIZED_USER);
        responseBody.put("data", null);
        responseBody.put("errors", null);

        // 3. Konversi Map ke JSON String dan kirim ke response stream
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), responseBody);
    }
}