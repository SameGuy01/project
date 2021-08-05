package com.kirill.kotlinblog.utils;

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.fasterxml.jackson.databind.ObjectMapper
import com.kirill.kotlinblog.service.UserService
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component
import java.util.*
import java.util.stream.Collectors
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtUtil(@Value("kotlin-blog.jwt.token-time") var timeProperty: String) {
    fun refreshToken(
            request:HttpServletRequest,
            response:HttpServletResponse,
            userService: UserService
    ) {
        val tokenTime = timeProperty.toLong()
        val authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION)
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                val refreshToken = authorizationHeader.substring("Bearer ".length)
                val algorithm = Algorithm.HMAC256("secret".toByteArray())
                val verifier = JWT.require(algorithm).build()
                val decodedJWT = verifier.verify(refreshToken)
                val username = decodedJWT.subject
                val user = userService.getUserByUsername(username)

                val accessToken = JWT.create()
                        .withSubject(user.username)
                        .withExpiresAt(Date(System.currentTimeMillis() + tokenTime))
                        .withIssuer(request.requestURL.toString())
                        .withClaim("roles", user.roles.stream().map { it.role.name }.collect(Collectors.toList()))
                    .sign(algorithm)

                val tokens = hashMapOf<String, String>()
                tokens["access_token"] = accessToken
                tokens["refresh_token"] = refreshToken
                response.contentType = MediaType.APPLICATION_JSON_VALUE
                ObjectMapper().writeValue(response.outputStream, tokens)
            } catch (e: Exception) {
                println("Error logging in: " + e.message)
                response.setHeader("error", e.message)
                response.status = HttpStatus.FORBIDDEN.value()
                val error = mutableMapOf<String, String>()
                error["error_message"] = e.message.toString()
                response.contentType = MediaType.APPLICATION_JSON_VALUE
                ObjectMapper().writeValue(response.outputStream, error)
            }
        } else {
            throw RuntimeException("Refresh token is missing")
        }
    }

    fun createTokenAfterAuth(
        authentication: Authentication,
        request: HttpServletRequest,
        response: HttpServletResponse
    ) {
        val tokenTime = timeProperty.toLong()
        val user = authentication.principal as User
        val algorithm = Algorithm.HMAC256("secret".toByteArray())

        val accessToken = JWT.create()
            .withSubject(user.username)
            .withExpiresAt(Date(System.currentTimeMillis() + tokenTime))
            .withIssuer(request.requestURL.toString())
            .withClaim(
                "roles",
                user.authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())
            )
            .sign(algorithm)

        val refreshToken = JWT.create()
            .withSubject(user.username)
            .withExpiresAt(Date(System.currentTimeMillis() + tokenTime))
            .withIssuer(request.requestURL.toString())
            .sign(algorithm)

        val tokens = hashMapOf<String, String>()
        tokens["access_token"] = accessToken
        tokens["refresh_token"] = refreshToken

        response.contentType = MediaType.APPLICATION_JSON_VALUE
        ObjectMapper().writeValue(response.outputStream, tokens)
    }
}
