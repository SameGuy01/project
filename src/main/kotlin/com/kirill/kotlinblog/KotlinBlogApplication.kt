package com.kirill.kotlinblog

import com.kirill.kotlinblog.domain.Role
import com.kirill.kotlinblog.domain.User
import com.kirill.kotlinblog.domain.enum.ERole
import com.kirill.kotlinblog.service.UserService
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder

@SpringBootApplication
class KotlinBlogApplication {
	@Bean
	fun passwordEncoder(): BCryptPasswordEncoder = BCryptPasswordEncoder()

	/*@Bean
	fun run(userService: UserService) = CommandLineRunner {
		userService.saveRole(Role(1, ERole.ROLE_ADMIN))
		userService.saveRole(Role(2, ERole.ROLE_USER))
		userService.saveRole(Role(3, ERole.ROLE_MODERATOR))

		userService.saveUser(User(1, "sameguy01", "1234", "kirill@gmail.com", mutableSetOf()))
		userService.saveUser(User(2, "chelik228", "123", "chelik@gmail.com", mutableSetOf()))
		userService.saveUser(User(3, "vasya", "12", "vasya@gmail.com", mutableSetOf()))

		userService.addRoleToUser("sameguy01", ERole.ROLE_ADMIN)
		userService.addRoleToUser("sameguy01", ERole.ROLE_MODERATOR)
		userService.addRoleToUser("sameguy01", ERole.ROLE_USER)
		userService.addRoleToUser("chelik228", ERole.ROLE_USER)
		userService.addRoleToUser("vasya", ERole.ROLE_MODERATOR)
		userService.addRoleToUser("vasya", ERole.ROLE_USER)
	}*/
}

fun main(args: Array<String>) {
	runApplication<KotlinBlogApplication>(*args)
}