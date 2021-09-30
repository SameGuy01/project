package com.dev.kotlinblog.controller

import com.dev.kotlinblog.domain.Role
import com.dev.kotlinblog.domain.User
import com.dev.kotlinblog.domain.dto.UpdateUserDto
import com.dev.kotlinblog.domain.enum.ERole
import com.dev.kotlinblog.service.UserService
import com.dev.kotlinblog.utils.JwtUtil
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@RestController
@RequestMapping("/api")
data class UserController(val userService: UserService,
                          val jwtUtil: JwtUtil
) {

    @PostMapping("/users/save")
    fun saveUser(@RequestBody user: User): ResponseEntity<*>{
        val uri = URI
            .create(ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/api/users/save")
            .toUriString())

        return ResponseEntity.created(uri).body(userService.saveUser(user))
    }

    @GetMapping("/users")
    fun getUsers():ResponseEntity<List<User>>{
        return ResponseEntity.ok().body(userService.getUsers())
    }

    @PostMapping("/role/save")
    fun saveRole(@RequestBody role: Role): ResponseEntity<Role>{
        val uri = URI
                .create(ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/role/save")
                .toUriString())

        return ResponseEntity.created(uri).body(userService.saveRole(role))
    }

    @PutMapping("/users/update")
    fun updateUser(@RequestBody user: User): ResponseEntity<User>{
        return ResponseEntity.ok().body(userService.update(user))
    }

    @PostMapping("/role/addtouser")
    fun addRoleToUser(@RequestBody form: RoleToUserForm): ResponseEntity<Unit>{
        userService.addRoleToUser(form.username, form.roleName)
        return ResponseEntity.ok().build()
    }

    @GetMapping("/token/refresh")
    fun refreshToken(request: HttpServletRequest, response: HttpServletResponse){
        jwtUtil.refreshToken(request, response, userService)
    }

    data class RoleToUserForm(
        val username:String,
        val roleName: ERole
    )
}