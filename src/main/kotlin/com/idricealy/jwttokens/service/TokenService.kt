package com.idricealy.jwttokens.service

import com.idricealy.jwttokens.config.JwtProperties
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.Date

@Service
class TokenService (
    jwtProperties: JwtProperties
) {

    // value to verify if message has not modify during the transfer, like handshake
    private val securityKey = Keys.hmacShaKeyFor(
        jwtProperties.key.toByteArray()
    )

    /**
     * Claims : information about the entity, can use this token
     * Subject : owner of the token
     * Issued at: the token start at this time
     * Compact : create the string value of token
     *
     * Add claim -> specify which claim we want to add -> and build the token
     */
    fun generate(
        userDetails: UserDetails,
        expirationDate: Date,
        additionalClaims: Map<String, Any> = emptyMap()
    ) : String =
        Jwts.builder()
            .claims()
            .subject(userDetails.username)
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(expirationDate)
            .add(additionalClaims)
            .and()
            .signWith(securityKey)
            .compact()


    /**
     * Subject is email of user automatically because he is the unique value of app
     */
    fun extractEmail(token: String): String? =
        getAllClaims(token)
            .subject

    fun isExpired(token: String): Boolean =
        getAllClaims(token)
            .expiration
            .before(Date(System.currentTimeMillis()))

    /**
     * Try to match if the current email extract from jwtToken equals the current user.
     * To know if jwt token is valid or not
     */
    fun isValid(token: String, userDetails: UserDetails) : Boolean {
        val email = extractEmail(token)

        return userDetails.username.equals(email) && !isExpired(token)
    }

    private fun getAllClaims(token: String) : Claims {
        val parser = Jwts.parser()
            .verifyWith(securityKey)
            .build()

        return parser.parseSignedClaims(token)
            .payload
    }


}