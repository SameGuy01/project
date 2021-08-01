package com.kirill.kotlinblog.domain

import javax.persistence.*

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id:Long,

    var username:String,

    var password:String,

    var email:String,

    @ManyToMany(fetch = FetchType.EAGER)
    var roles:MutableCollection<Role> = mutableSetOf()
)