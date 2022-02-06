package com.dev.kotlinblog.service

import com.kirill.kotlinblog.repository.UserRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
data class UserDetailsServiceImpl(val userRepo: UserRepository) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepo.findByUsername(username)

        val authorities = mutableSetOf<SimpleGrantedAuthority>()
        user.roles.forEach {
            authorities.add(SimpleGrantedAuthority(it.role.name))
        }

        return org.springframework.security.core.userdetails.User(user.username,user.password,authorities)
    }
}