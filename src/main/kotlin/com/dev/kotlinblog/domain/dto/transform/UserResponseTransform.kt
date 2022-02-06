package com.kirill.kotlinblog.domain.dto.transform

import com.kirill.kotlinblog.domain.User
import com.kirill.kotlinblog.domain.dto.UserResponse

class UserResponseTransform : Transform<User,UserResponse> {
    override fun transform(source: User): UserResponse = UserResponse(source.id,source.username)
}