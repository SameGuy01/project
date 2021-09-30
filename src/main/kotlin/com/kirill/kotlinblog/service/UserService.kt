package com.kirill.kotlinblog.service

import com.kirill.kotlinblog.domain.Role
import com.kirill.kotlinblog.domain.User
import com.kirill.kotlinblog.domain.enum.ERole
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

interface UserService{
    fun saveRole(role: Role): Role
    fun getUserById(id:Long): ResponseEntity<*>
    fun getUserByUsername(username: String): User
    fun getUsers():List<User>
    fun saveUser(user : User): User
    fun deleteById(id: Long): ResponseEntity<*>
    fun update(user: User): User
    fun addRoleToUser(username:String,roleName:ERole)
}