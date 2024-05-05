package com.idricealy.jwttokens.network

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

/**
 * This method allow to avoid CORS POLICY error from front-end
 */
@Configuration
class WebMvcConfig : WebMvcConfigurer {
    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
            .allowedOriginPatterns("http://localhost:3000", "http://localhost:5173")
            .allowedMethods("GET", "POST", "DELETE", "PUT")
            .allowCredentials(true);
    }
}