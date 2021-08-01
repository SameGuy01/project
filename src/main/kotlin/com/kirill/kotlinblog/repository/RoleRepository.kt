package com.kirill.kotlinblog.repository

import com.kirill.kotlinblog.domain.Role
import com.kirill.kotlinblog.domain.enum.ERole
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RoleRepository : JpaRepository<Role,Long> {
    fun findByRole(role: ERole):Role
    fun save(role:Role): Role
}