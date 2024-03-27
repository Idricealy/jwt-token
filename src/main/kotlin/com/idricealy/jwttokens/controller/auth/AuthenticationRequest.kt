package com.idricealy.jwttokens.controller.auth

data class AuthenticationRequest(
    val email: String,
    val password: String
)
