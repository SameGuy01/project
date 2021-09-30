package com.dev.kotlinblog.domain

import com.dev.kotlinblog.domain.enum.ERole
import javax.persistence.*

@Entity
data class Role(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id:Long,

    @Enumerated
    val role: ERole
) {
}