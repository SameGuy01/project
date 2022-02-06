package com.kirill.kotlinblog.controller

import com.kirill.kotlinblog.domain.Role
import com.kirill.kotlinblog.domain.UserSaveDTO
import com.kirill.kotlinblog.domain.dto.UserResponse
import com.kirill.kotlinblog.domain.enum.ERole
import com.kirill.kotlinblog.service.UserService
import com.kirill.kotlinblog.utils.JwtUtil
import com.kirill.kotlinblog.utils.validation.UserValidation
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/api")
data class UserController(val userService: UserService,
                          val jwtUtil: JwtUtil,
                          val userValidation: UserValidation) {

    @PostMapping("/users/save")
    fun saveUser(@RequestBody user: UserSaveDTO): ResponseEntity<*>{
        val uri = URI
            .create(ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/api/users/save")
            .toUriString())
        userValidation.userSaveValidate(user)
        return ResponseEntity.created(uri).body(userService.saveUser(user))
    }

    @PostMapping("/users/update")
    fun updateUser(@RequestBody user: UserSaveDTO): ResponseEntity<*>{
        userValidation.userSaveValidate()
        return ResponseEntity.ok().body(userService.update(user))
    }

    @GetMapping("/users")
    fun getUsers(): ResponseEntity<List<UserResponse>>{
        return ResponseEntity.ok().body(userService.getUsers())
    }

    @PostMapping("/role/save")
    fun saveRole(@RequestBody role:Role): ResponseEntity<Role>{
        val uri = URI
                .create(ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/role/save")
                .toUriString())

        return ResponseEntity.created(uri).body(userService.saveRole(role))
    }

    @PostMapping("/role/addtouser")
    fun addRoleToUser(@RequestBody form:RoleToUserForm): ResponseEntity<Any>{
        userService.addRoleToUser(form.username, form.roleName)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/token/refresh")
    fun refreshToken(request: HttpServletRequest, response: HttpServletResponse){
        jwtUtil.refreshToken(request, response, userService)
    }

    data class RoleToUserForm(
        val username:String,
        val roleName:ERole
    )
}