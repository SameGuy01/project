package com.kirill.kotlinblog.domain

import javax.persistence.*

@Entity
@Table(name = "users")
data class User (
    var username: String
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id:Long = 0

    var password:String = ""

    var email:String = ""

    @ManyToMany(fetch = FetchType.EAGER)
    var roles:MutableCollection<Role> = mutableSetOf()
}

data class UserSaveDTO(
    val id: Long = 0,
    val username: String,
    val password: String,
    val email: String
)