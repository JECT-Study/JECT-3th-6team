package com.example.demo.common.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.List;

@Slf4j
@Component
public class RedirectUrlValidator {

    private final Environment environment;

    public RedirectUrlValidator(Environment environment) {
        this.environment = environment;
    }

    /**
     * 리다이렉트 URL이 허용된 도메인인지 검증합니다.
     * @param url 검증할 URL
     * @return 허용된 URL인 경우 true
     */
    public boolean isAllowedRedirect(String url) {
        if (url == null || url.trim().isEmpty()) {
            return false;
        }

        try {
            URI uri = URI.create(url);
            String scheme = uri.getScheme();
            String host = uri.getHost();
            int port = uri.getPort();

            // 허용된 스킴만 허용
            if (!List.of("http", "https").contains(scheme)) {
                log.warn("Invalid scheme in redirect URL: {}", scheme);
                return false;
            }

            // 로컬 개발 환경 허용
            boolean isLocal = isLocalDevelopment(host, port);
            
            // 개발/운영 환경 허용
            boolean isAllowedDomain = isAllowedDomain(host);

            boolean allowed = isLocal || isAllowedDomain;
            
            if (!allowed) {
                log.warn("Redirect URL not in whitelist: {}", url);
            }

            return allowed;

        } catch (IllegalArgumentException e) {
            log.warn("Invalid redirect URL format: {}", url, e);
            return false;
        }
    }

    /**
     * 검증된 리다이렉트 URL을 반환합니다. 검증 실패 시 환경에 맞는 기본 URL을 반환합니다.
     * @param url 검증할 URL
     * @return 검증된 URL 또는 환경에 맞는 기본 URL
     */
    public String getValidatedRedirectUrl(String url) {
        if (isAllowedRedirect(url)) {
            return url;
        }
        String fallbackUrl = getEnvironmentSpecificDefaultUrl();
        return fallbackUrl;
    }

    private boolean isLocalDevelopment(String host, int port) {
        if (host == null) return false;
        
        // localhost, 127.0.0.1 허용
        boolean isLocalhost = "localhost".equals(host) || "127.0.0.1".equals(host);
        
        // 허용된 포트들 (개발용)
        List<Integer> allowedPorts = List.of(-1, 3000, 5173, 8080, 8081);
        boolean isAllowedPort = allowedPorts.contains(port);
        
        return isLocalhost && isAllowedPort;
    }

    private boolean isAllowedDomain(String host) {
        if (host == null) return false;
        
        // 환경별 허용 도메인 설정
        List<String> allowedDomains = getAllowedDomainsForCurrentEnvironment();
        
        return allowedDomains.contains(host);
    }
    
    /**
     * 현재 환경에 따른 허용 도메인 목록을 반환합니다.
     * @return 허용된 도메인 목록
     */
    private List<String> getAllowedDomainsForCurrentEnvironment() {
        if (environment.acceptsProfiles(Profiles.of("dev"))) {
            // 개발 환경: dev 도메인과 로컬 개발 도메인만 허용
            return List.of(
                "dev.spotit.co.kr"
            );
        } else if (environment.acceptsProfiles(Profiles.of("prod"))) {
            // 운영 환경: 운영 도메인만 허용
            return List.of(
                "spotit.co.kr",
                "www.spotit.co.kr"
            );
        } else {
            // 로컬 환경: 로컬 개발 도메인만 허용
            return List.of(
                "localhost",
                "127.0.0.1"
            );
        }
    }
    
    /**
     * 현재 환경에 맞는 기본 URL을 반환합니다.
     * @return 환경별 기본 URL
     */
    private String getEnvironmentSpecificDefaultUrl() {
        if (environment.acceptsProfiles(Profiles.of("dev"))) {
            // 개발 환경: dev 도메인 사용
            return "https://dev.spotit.co.kr";
        } else if (environment.acceptsProfiles(Profiles.of("prod"))) {
            // 운영 환경: 운영 도메인 사용
            return "https://spotit.co.kr";
        } else {
            // 로컬 환경: 로컬 개발 URL 사용
            return "http://localhost:3000";
        }
    }
}

