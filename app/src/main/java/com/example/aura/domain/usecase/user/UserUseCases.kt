package com.example.aura.domain.usecase.user

import com.example.aura.domain.model.User
import com.example.aura.domain.repository.UserRepository

class GetUserProfileUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(): User = repository.getUserProfile()
}

class UpdateUserProfileUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(user: User): User {
        require(user.name.isNotBlank()) { "O nome do usuário é obrigatório." }
        return repository.updateUserProfile(user)
    }
}

class DeleteAccountUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(userId: String) = repository.deleteAccount(userId)
}

class CheckPremiumStatusUseCase(private val repository: UserRepository) {
    suspend operator fun invoke(): Boolean = repository.isUserPremium()
}

data class UserUseCases(
    val getProfile: GetUserProfileUseCase,
    val updateProfile: UpdateUserProfileUseCase,
    val deleteAccount: DeleteAccountUseCase,
    val checkPremiumStatus: CheckPremiumStatusUseCase
)
