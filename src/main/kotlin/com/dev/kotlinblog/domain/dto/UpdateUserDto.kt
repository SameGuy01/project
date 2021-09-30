package com.dev.kotlinblog.domain.dto

class UpdateUserDto(
    val oldUsername: String,
    val username: String? = null,
    val password: String? = null,
    val email: String? = null
) {
}