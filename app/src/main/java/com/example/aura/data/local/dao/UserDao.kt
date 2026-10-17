package com.example.aura.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.aura.data.local.entity.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM user LIMIT 1")
    fun observeUser(): Flow<UserEntity?>

    @Query("SELECT * FROM user LIMIT 1")
    suspend fun getUser(): UserEntity?

    @Query("UPDATE user SET profile = :profile, preferredFilters = :filters")
    suspend fun updateProfile(profile: String?, filters: List<String>)

    @Query("UPDATE user SET name = :name")
    suspend fun updateName(name: String)

    @Query("DELETE FROM user")
    suspend fun clearUser()
}
