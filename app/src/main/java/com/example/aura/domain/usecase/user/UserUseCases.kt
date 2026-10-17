package com.example.aura.domain.usecase.user

import com.example.aura.domain.model.AccessibilityFilter
import com.example.aura.domain.model.AccessibilityProfile
import com.example.aura.domain.model.User
import com.example.aura.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow

class LoginUserUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke(name: String, email: String, profile: AccessibilityProfile?): User {
        require(name.isNotBlank()) { "Informe seu nome." }
        require(email.isBlank() || EMAIL_REGEX.matches(email.trim())) { "Informe um e-mail válido." }
        return userRepository.login(name.trim(), email.trim(), profile)
    }

    private companion object {
        val EMAIL_REGEX = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")
    }
}

class LogoutUserUseCase(private val userRepository: UserRepository) {
    suspend operator fun invoke() = userRepository.logout()
}

class GetLocalUserUseCase(private val repository: UserRepository) {
    operator fun invoke(): Flow<User?> = repository.observeUser()
}

class UpdateAccessibilityProfileUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(profile: AccessibilityProfile?, preferredFilters: Set<AccessibilityFilter>) =
        repository.updateProfile(profile, preferredFilters)
}

class UpdateUserNameUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(name: String) {
        require(name.isNotBlank()) { "Informe seu nome." }
        repository.updateName(name.trim())
    }
}

data class UserUseCases(
    val loginUser: LoginUserUseCase,
    val logoutUser: LogoutUserUseCase,
    val getLocalUser: GetLocalUserUseCase,
    val updateAccessibilityProfile: UpdateAccessibilityProfileUseCase,
    val updateUserName: UpdateUserNameUseCase,
)
