package com.kirill.kotlinblog.security

import com.kirill.kotlinblog.filter.CustomAuthenticationFilter
import com.kirill.kotlinblog.filter.CustomAuthorizationFilter
import com.kirill.kotlinblog.utils.JwtUtil
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@EnableWebSecurity
data class SecurityConfig(
    val userDetailsService: UserDetailsService,
    val bCryptPasswordEncoder: BCryptPasswordEncoder,
    val jwtUtils: JwtUtil) : WebSecurityConfigurerAdapter(){


    override fun configure(auth: AuthenticationManagerBuilder) {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder)
    }

    override fun configure(http: HttpSecurity) {
        val customAuthenticationFilter = CustomAuthenticationFilter(authenticationManagerBean(),jwtUtils)
        customAuthenticationFilter.setFilterProcessesUrl("/api/login")
        http.logout().logoutUrl("/api/logout")
        http.csrf().disable()
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        http.authorizeRequests().antMatchers("/api/login/**","/api/token/refresh/**","/api/logout").permitAll()
        http.authorizeRequests().antMatchers(HttpMethod.GET,"/api/users/**").hasAnyAuthority("ROLE_USER")
        http.authorizeRequests().antMatchers(HttpMethod.POST,"/api/users/save/**").permitAll()
        //.hasAnyAuthority("ROLE_ADMIN")
        http.authorizeRequests().anyRequest().authenticated()
        http.addFilter(customAuthenticationFilter)
        http.addFilterBefore(CustomAuthorizationFilter(),UsernamePasswordAuthenticationFilter::class.java)
    }

    @Bean
    override fun authenticationManagerBean():AuthenticationManager{
        return super.authenticationManagerBean()
    }
}