package com.example.aura.domain.usecase.user

import com.example.aura.domain.model.User
import com.example.aura.domain.repository.UserRepository

class LoginUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(email: String, password: String): User? {
        return userRepository.loginUser(email, password)
    }
}

data class UserUseCases(
    val loginUser: LoginUseCase
)
