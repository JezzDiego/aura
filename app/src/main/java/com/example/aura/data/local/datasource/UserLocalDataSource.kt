package com.example.aura.data.local.datasource

import com.example.aura.data.local.dao.UserDao
import com.example.aura.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

class UserLocalDataSource (private val dao: UserDao) {
    suspend fun saveUser(user: UserEntity) = dao.insertUser(user)

    suspend fun clearUser(){
        dao.clearUser()
    }

    fun getUser(): Flow<UserEntity?> = dao.getUser()
}