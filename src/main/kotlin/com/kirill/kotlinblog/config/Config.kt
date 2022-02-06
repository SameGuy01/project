package com.kirill.kotlinblog.config

import org.modelmapper.ModelMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.PropertySource
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@Configuration
@ComponentScan("com.kirill.kotlinblog")
class Config {
    @Bean
    fun modelMapper(): ModelMapper = ModelMapper()
}