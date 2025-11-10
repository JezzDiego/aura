package com.example.aura.domain.usecase.user

import com.example.aura.domain.model.User
import com.example.aura.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class LoginUserUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(email: String, password: String): User? = userRepository.loginUser(email, password)

}

class LogoutUserUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(): Unit = userRepository.logout()

}

class GetUserListUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(): List<User> = repository.getAllUsers()
}

class GetLocalUserUseCase(private val repository: UserRepository) {
    operator fun invoke(): Flow<User?> = repository.observeUser()
}

data class UserUseCases(
    val loginUser: LoginUserUseCase,
    val logoutUser: LogoutUserUseCase,
    val getAllUsers: GetUserListUseCase,
    val getLocalUser: GetLocalUserUseCase
)
