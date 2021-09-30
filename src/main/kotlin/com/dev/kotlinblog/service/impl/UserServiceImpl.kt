package com.dev.kotlinblog.service.impl

import com.dev.kotlinblog.domain.Role
import com.dev.kotlinblog.domain.User
import com.dev.kotlinblog.domain.dto.UpdateUserDto
import com.dev.kotlinblog.domain.enum.ERole
import com.dev.kotlinblog.repository.RoleRepository
import com.dev.kotlinblog.repository.UserRepository
import com.dev.kotlinblog.service.UserService
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class UserServiceImpl(val userRepo: UserRepository,
                      val roleRepo: RoleRepository,
                      val passwordEncoder: PasswordEncoder) : UserService {

    override fun saveRole(role: Role): Role {
        return roleRepo.save(role)
    }

    override fun getUserById(id:Long): ResponseEntity<*> {
        if (userRepo.findById(id).isEmpty){
            return ResponseEntity.badRequest().body("No such user with id "+id)
        }
        return ResponseEntity.ok().body(userRepo.findById(id).orElseThrow())
    }

    override fun getUserByUsername(username: String): User? {
        return userRepo.findByUsername(username)
    }

    override fun getUsers(): List<User> = userRepo.findAll()

    override fun saveUser(user: User): User {
        if (userRepo.findByUsername(user.username).equals(user.username)){
            throw RuntimeException("User with such username already exists")
        }

        user.password = passwordEncoder.encode(user.password)
        user.roles.add(roleRepo.findByRole(ERole.ROLE_USER))
        return userRepo.save(user)
    }

    override fun deleteById(id: Long) :ResponseEntity<*>{
        if (userRepo.findById(id).isEmpty){
            throw RuntimeException("No user found with such id: $id")
        }
        userRepo.deleteById(id)
        return ResponseEntity.ok().body("User with id: "+id+"successfully removed")
    }

    override fun update(user: User): User? {
        return userRepo.save(user)
    }

    override fun addRoleToUser(username: String, roleName: ERole) {
        val user = userRepo.findByUsername(username)
        val role = roleRepo.findByRole(roleName)
        user.roles.add(role)
    }
}