package com.example.aura.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.example.aura.data.local.entity.AccessibilityFeatureEntity
import com.example.aura.data.local.entity.EstablishmentEntity
import com.example.aura.data.local.entity.EstablishmentPhotoEntity
import com.example.aura.data.local.entity.EstablishmentWithDetails

@Dao
interface EstablishmentDao {

    @Query("SELECT COUNT(*) FROM establishments")
    suspend fun count(): Int

    @Transaction
    @Query("SELECT * FROM establishments ORDER BY distanceMeters ASC")
    suspend fun getAll(): List<EstablishmentWithDetails>

    @Transaction
    @Query("SELECT * FROM establishments WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): EstablishmentWithDetails?

    @Transaction
    @Query(
        """
        SELECT * FROM establishments
        WHERE name LIKE '%' || :query || '%'
           OR category LIKE '%' || :query || '%'
           OR categoryDetail LIKE '%' || :query || '%'
           OR neighborhood LIKE '%' || :query || '%'
           OR address LIKE '%' || :query || '%'
        ORDER BY distanceMeters ASC
        """
    )
    suspend fun search(query: String): List<EstablishmentWithDetails>

    @Transaction
    @Query("SELECT * FROM establishments WHERE isFavorite = 1 ORDER BY name ASC")
    suspend fun getFavorites(): List<EstablishmentWithDetails>

    @Query("UPDATE establishments SET isFavorite = :favorite WHERE id = :id")
    suspend fun setFavorite(id: String, favorite: Boolean)

    @Query("UPDATE establishments SET rating = :rating, reviewCount = :reviewCount WHERE id = :id")
    suspend fun updateRating(id: String, rating: Double, reviewCount: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEstablishments(items: List<EstablishmentEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFeatures(items: List<AccessibilityFeatureEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPhotos(items: List<EstablishmentPhotoEntity>)

    @Query("DELETE FROM establishments")
    suspend fun clearAll()

    @Transaction
    suspend fun insertWithDetails(
        establishments: List<EstablishmentEntity>,
        features: List<AccessibilityFeatureEntity>,
        photos: List<EstablishmentPhotoEntity>,
    ) {
        insertEstablishments(establishments)
        if (features.isNotEmpty()) insertFeatures(features)
        if (photos.isNotEmpty()) insertPhotos(photos)
    }
}
