package org.pda.announcement.util.security.cookie;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import java.util.HashMap;
import java.util.Map;

public class CustomHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private final Map<String, String> customHeaders;

    public CustomHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
        customHeaders = new HashMap<>();
    }

    // 기존 헤더를 복사하여 추가하거나 수정
    public void addHeader(String name, String value) {
        customHeaders.put(name, value);
    }

    @Override
    public String getHeader(String name) {
        String headerValue = customHeaders.get(name);
        if (headerValue != null) {
            return headerValue;
        }
        return super.getHeader(name);
    }

    @Override
    public java.util.Enumeration<String> getHeaders(String name) {
        // Custom headers가 있다면 그 값을 우선 반환
        if (customHeaders.containsKey(name)) {
            return java.util.Collections.enumeration(java.util.Collections.singletonList(customHeaders.get(name)));
        }
        return super.getHeaders(name);
    }

    @Override
    public java.util.Enumeration<String> getHeaderNames() {
        // 모든 헤더 이름을 반환
        java.util.List<String> headerNames = java.util.Collections.list(super.getHeaderNames());
        headerNames.addAll(customHeaders.keySet());
        return java.util.Collections.enumeration(headerNames);
    }
}
