package com.example.aura.data.local.datasource

import com.example.aura.data.local.dao.UserDao
import com.example.aura.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

class UserLocalDataSource(private val dao: UserDao) {

    suspend fun saveUser(user: UserEntity) = dao.insertUser(user)

    suspend fun getUser(): UserEntity? = dao.getUser()

    fun observeUser(): Flow<UserEntity?> = dao.observeUser()

    suspend fun updateProfile(profile: String?, filters: List<String>) = dao.updateProfile(profile, filters)

    suspend fun updateName(name: String) = dao.updateName(name)

    suspend fun clearUser() = dao.clearUser()
}
