package com.kirill.kotlinblog.filter

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import java.util.Arrays.stream
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Configuration
class CustomAuthorizationFilter : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        if (request.servletPath.equals("/api/login") || request.servletPath.equals("/api/token/refresh")){
            filterChain.doFilter(request,response)
        } else {
            val authorizationHeader = request.getHeader(AUTHORIZATION)
            if (authorizationHeader!=null && authorizationHeader.startsWith("Bearer ")){
                try {
                    val token = authorizationHeader.substring("Bearer ".length)
                    val algorithm = Algorithm.HMAC256("secret".toByteArray())
                    val verifier =  JWT.require(algorithm).build()
                    val decodedJWT = verifier.verify(token)
                    val username = decodedJWT.subject

                    val roles = decodedJWT.getClaim("roles").asArray(String::class.java)
                    val authorities = mutableSetOf<SimpleGrantedAuthority>()
                    stream(roles).forEach {
                        authorities.add(SimpleGrantedAuthority(it))
                    }
                    val authenticationToken = UsernamePasswordAuthenticationToken(username,null,authorities)
                    SecurityContextHolder.getContext().authentication = authenticationToken
                    filterChain.doFilter(request,response)
                } catch (e: Exception){
                    println("Error logging in: "+e.message)
                    response.setHeader(HttpStatus.UNAUTHORIZED.name,e.message)
                    response.status = HttpStatus.FORBIDDEN.value()
                    response.contentType = MediaType.APPLICATION_JSON_VALUE

                    //response.sendError(HttpStatus.FORBIDDEN.value())
                    val error = mapOf("error_message" to e.message.toString())
                    ObjectMapper().writeValue(response.outputStream,error)
                }
            }else{
                filterChain.doFilter(request,response)
            }
        }
    }
}