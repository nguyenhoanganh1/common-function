package com.tech.common.configuration;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * The type Web mvc config util.
 */
public class WebMvcConfigUtil {

    /**
     * Createc cors configuration source cors configuration source.
     *
     * @param origins the origins
     * @return the cors configuration source
     */
    public static CorsConfigurationSource createcCorsConfigurationSource(List<String> origins) {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedOriginPatterns(origins);
        corsConfiguration.addAllowedHeader(CorsConfiguration.ALL);
        corsConfiguration.addAllowedMethod(CorsConfiguration.ALL);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

}
