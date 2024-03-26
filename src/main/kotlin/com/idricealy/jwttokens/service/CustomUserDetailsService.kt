package com.idricealy.jwttokens.service

import com.idricealy.jwttokens.repository.UserRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

/**
 * Alias because we've got both class that we want to use,
 * our User model, and the User class from Spring
 */
typealias ApplicationUser = com.idricealy.jwttokens.model.User

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository

) : UserDetailsService{


    override fun loadUserByUsername(username: String): UserDetails =
        userRepository.findByEmail(username)
            ?.mapToUserDetails()
            ?: throw UsernameNotFoundException("Not found!")

    private fun ApplicationUser.mapToUserDetails() : UserDetails =
        User.builder()
            .username(this.email)
            .password(this.password)
            .roles(this.role.name)
            .build()
}