package com.kirill.kotlinblog.controller

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.fasterxml.jackson.databind.ObjectMapper
import com.kirill.kotlinblog.domain.Role
import com.kirill.kotlinblog.domain.User
import com.kirill.kotlinblog.domain.enum.ERole
import com.kirill.kotlinblog.service.UserService
import com.kirill.kotlinblog.utils.JwtUtil
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.security.core.GrantedAuthority
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI
import java.util.*
import java.util.stream.Collectors
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/api")
data class UserController(val userService: UserService,
                          val jwtUtil: JwtUtil) {

    @PostMapping("/users/save")
    fun saveUser(@RequestBody user:User): ResponseEntity<*>{
        val uri = URI
            .create(ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/api/user/save")
            .toUriString())

        return ResponseEntity.created(uri).body(userService.saveUser(user))
    }

    @GetMapping("/users")
    fun getUsers():ResponseEntity<List<User>>{
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
    fun addRoleToUser(@RequestBody form:RoleToUserForm): ResponseEntity<Unit>{
        userService.addRoleToUser(form.username, form.roleName)
        return ResponseEntity.ok().build()
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