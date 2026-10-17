package com.example.aura.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.aura.data.local.dao.EstablishmentDao
import com.example.aura.data.local.dao.ReviewDao
import com.example.aura.data.local.dao.UserDao
import com.example.aura.data.local.entity.AccessibilityFeatureEntity
import com.example.aura.data.local.entity.EstablishmentEntity
import com.example.aura.data.local.entity.EstablishmentPhotoEntity
import com.example.aura.data.local.entity.ReviewEntity
import com.example.aura.data.local.entity.UserEntity

@Database(
    entities = [
        EstablishmentEntity::class,
        AccessibilityFeatureEntity::class,
        EstablishmentPhotoEntity::class,
        ReviewEntity::class,
        UserEntity::class,
    ],
    version = 4,
    exportSchema = false,
)
@TypeConverters(Converters::class)
abstract class AuraDatabase : RoomDatabase() {
    abstract fun establishmentDao(): EstablishmentDao
    abstract fun reviewDao(): ReviewDao
    abstract fun userDao(): UserDao
}
