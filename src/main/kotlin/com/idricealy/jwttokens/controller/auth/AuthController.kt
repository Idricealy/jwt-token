package com.idricealy.jwttokens.controller.auth

import com.idricealy.jwttokens.service.AuthenticationService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authenticationService: AuthenticationService
) {

    @PostMapping
    fun authenticate(@RequestBody authRequest: AuthenticationRequest) : AuthenticationResponse =
        authenticationService.authentication(authRequest)

    @PostMapping("/refresh")
    fun refreshAccessToken(
        @RequestBody request: RefreshTokenRequest
    ) : TokenResponse {

        print("here")
        return authenticationService.refreshAccessToken(request.token)
            ?.mapToTokensResponse()
            ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid refrehs token!")
    }


    private fun String.mapToTokensResponse(): TokenResponse =
        TokenResponse(
            token = this
        )

}