package com.kirill.kotlinblog.domain

import com.kirill.kotlinblog.domain.enum.ERole
import javax.persistence.*

@Entity
@Table(name="role")
data class Role(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id:Long,

    @Enumerated(EnumType.STRING)
    val role:ERole) {
}