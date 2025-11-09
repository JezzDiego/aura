package com.example.aura.data.local.datasource

import com.example.aura.data.local.dao.UserDao
import com.example.aura.data.local.entity.UserEntity

class UserLocalDataSource (private val dao: UserDao) {
    suspend fun saveUser(user: UserEntity) = dao.insertUser(user)

    suspend fun getUser() = dao.getUser()
}