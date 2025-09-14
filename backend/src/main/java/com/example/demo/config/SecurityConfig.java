package com.example.demo.config;

import com.example.demo.common.jwt.JwtAuthenticationFilter;
import com.example.demo.common.jwt.JwtProperties;
import com.example.demo.common.jwt.JwtTokenProvider;
import com.example.demo.common.security.CustomAccessDeniedHandler;
import com.example.demo.common.security.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableConfigurationProperties({JwtProperties.class, AppProperties.class, GmailSmtpProperties.class})
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final AppProperties appProperties;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;
    private final Environment environment;
    @Value("${custom.metrics.userName:username}")
    private String metricUsername;
    @Value("${custom.metrics.password:password}")
    private String metricPassword;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails admin = User.builder()
                .username(metricUsername)
                .password(passwordEncoder().encode(metricPassword))
                .roles("ACTUATOR")
                .build();

        return new InMemoryUserDetailsManager(admin);
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // application.yml에서 설정된 CORS 허용 도메인들 사용
        configuration.setAllowedOrigins(appProperties.getCorsAllowedOrigins());

        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true); // 쿠키 인증을 위해 필수
        configuration.setExposedHeaders(Arrays.asList("Authorization", "Content-Type", "Set-Cookie"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(httpBasic -> httpBasic
                        .realmName("Actuator")
                )
                //h2 확인을 위한 설정
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/error",
                                "/favicon.ico",
                                "/h2-console/**",
                                "/oauth/**", // /oauth/kakao/callback 등을 허용
                                "/static/**",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/uploads/**", // 업로드된 이미지 파일 접근 허용
                                "/index.html",
                                "/index.js",
                                "/admin-popup-create.html",
                                "/admin-popup-create.js"
                        ).permitAll()
                                // Swagger UI 관련 경로 허용 (dev 환경에서만)
                                .requestMatchers(request -> environment.acceptsProfiles(Profiles.of("dev")) && isSwaggerPath(request.getRequestURI())).permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/popups/**").permitAll() // GET 요청 허용
                        .requestMatchers(HttpMethod.POST, "/api/popups").permitAll() // 임시: 팝업 생성 무인증 허용
                        .requestMatchers(HttpMethod.POST, "/api/images/upload").permitAll() // 임시: 이미지 업로드 무인증 허용
                        .requestMatchers("/actuator/**").hasRole("ACTUATOR")
                        .anyRequest().authenticated()
                )
                .exceptionHandling(exceptions -> exceptions
                        .authenticationEntryPoint(customAuthenticationEntryPoint)
                        .accessDeniedHandler(customAccessDeniedHandler)
                )
                .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }


    /**
     * Swagger 관련 경로인지 확인
     */
    private boolean isSwaggerPath(String requestURI) {
        return requestURI.startsWith("/swagger-ui") || 
               requestURI.startsWith("/v3/api-docs") || 
               requestURI.equals("/swagger-ui.html");
    }
} 