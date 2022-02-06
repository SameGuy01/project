package com.kirill.kotlinblog.service

import com.kirill.kotlinblog.domain.Role
import com.kirill.kotlinblog.domain.User
import com.kirill.kotlinblog.domain.UserSaveDTO
import com.kirill.kotlinblog.domain.dto.UserResponse
import com.kirill.kotlinblog.domain.enum.ERole
import com.kirill.kotlinblog.repository.RoleRepository
import com.kirill.kotlinblog.repository.UserRepository
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

interface UserService{
    fun saveRole(role: Role): Role
    fun getUserById(id:Long): User?
    fun getUserByUsername(username: String): User?
    fun getUserByEmail(email: String): User?
    fun getUsers(): List<UserResponse>
    fun saveUser(user : UserSaveDTO): User
    fun deleteById(id: Long): User?
    fun update(user: User): User
    fun addRoleToUser(username:String,roleName:ERole)
}

@Service
@Transactional
class UserServiceImpl(val userRepo : UserRepository,
                      val roleRepo:RoleRepository,
                      val passwordEncoder: PasswordEncoder) : UserService {

    override fun saveRole(role: Role): Role {
        return roleRepo.save(role)
    }

    override fun getUserById(id:Long): User? {
        return userRepo.findById(id).orElse(null)
    }

    override fun getUserByUsername(username: String): User? {
        return userRepo.findUserByEmail(username).orElse(null)
    }

    override fun getUsers(): List<UserResponse> {
        return userRepo.findAll().map { UserResponse(it.id, it.username) }

    }

    override fun saveUser(userDto: UserSaveDTO): User {
        val userSave = User(userDto.username).apply {
            this.username = userDto.username
            this.email = userDto.email
            this.roles.add(roleRepo.findByRole(ERole.ROLE_USER))
        }
        return userRepo.save(userSave)
    }

    override fun deleteById(id: Long) :ResponseEntity<*>{
        userRepo.deleteById(id)
        return ResponseEntity.ok().body("User with id: "+id+"successfully removed")
    }

    override fun update(user: User): User {
        return userRepo.save(user)
    }

    override fun addRoleToUser(username: String, roleName: ERole) {
        val user = userRepo.findByUsername(username)
        val role = roleRepo.findByRole(roleName)
        user.roles.add(role)
    }

    fun getUserByEmail(email: String): User? {
        return userRepo.findUserByEmail(email).orElse(null)
    }
}