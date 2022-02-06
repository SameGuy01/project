package com.kirill.kotlinblog.utils.validation

import com.kirill.kotlinblog.domain.UserSaveDTO
import com.kirill.kotlinblog.service.UserService
import org.springframework.stereotype.Component

@Component
class UserValidation(val userService: UserService) {

    fun userSaveValidate(userDTO: UserSaveDTO){
        if (userService.getUserByUsername(userDTO.username) != null) {
            throw RuntimeException("User with ${userDTO.username} already exists")
        }

        if (userService.getUserByEmail(userDTO.email) != null) {
            throw RuntimeException("User with ${userDTO.email} already exists")
        }
    }

    fun userUpdateValidate(userDTO: UserSaveDTO){
        val currentUser = userService.getUserById(userDTO.id)

        userService.getUserByEmail(userDTO.email)?.let{
            //TODO
        }


    }
}