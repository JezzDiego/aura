package com.example.aura.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.aura.data.local.entity.ReviewEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReviewDao {

    @Query("SELECT COUNT(*) FROM reviews")
    suspend fun count(): Int

    @Query("SELECT * FROM reviews ORDER BY createdAt DESC LIMIT :limit")
    fun observeRecent(limit: Int): Flow<List<ReviewEntity>>

    @Query("SELECT * FROM reviews WHERE establishmentId = :establishmentId ORDER BY createdAt DESC")
    suspend fun getByEstablishment(establishmentId: String): List<ReviewEntity>

    @Query("SELECT * FROM reviews WHERE authorId = :authorId ORDER BY createdAt DESC")
    suspend fun getByAuthor(authorId: String): List<ReviewEntity>

    @Query("SELECT * FROM reviews WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): ReviewEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(review: ReviewEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(reviews: List<ReviewEntity>)

    @Query("UPDATE reviews SET helpfulCount = :count, markedHelpful = :marked WHERE id = :id")
    suspend fun updateHelpful(id: String, count: Int, marked: Boolean)
}
