package com.idricealy.jwttokens.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfiguration (
    private val authenticationProvider: AuthenticationProvider
){

    /**
     * Method to tell which endpoint is authorized
     * Careful when disable csrf /!\
     * /error -> spring automatically throw 403 error so to allow us
     * to throw any 40* request we need to match the url
     */
    @Bean
    fun securityFilterchain(
        http: HttpSecurity,
        jwtAuthenticationFilter: JwtAuthenticationFilter
    ) : DefaultSecurityFilterChain =
        http
            .csrf { it.disable() }
            // who and how can access to API
            .authorizeHttpRequests {
                it
                    .requestMatchers("/api/auth", "/api/auth/refresh", "/error")
                    .permitAll()
                    .requestMatchers(HttpMethod.POST, "/api/user")
                    .permitAll()
                    .requestMatchers("/api/user**")
                    .hasRole("ADMIN")
                    .anyRequest()
                    .fullyAuthenticated()
            }
            //stateless – No session will be created or used by Spring Security.
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authenticationProvider(authenticationProvider)
            // add our filter before the real authfilter
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            .build()
}