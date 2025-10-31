package com.example.aura.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.aura.data.local.dao.ExamDao
import com.example.aura.data.local.dao.LaboratoryDao
import com.example.aura.data.local.entity.*

@Database(
    entities = [ExamEntity::class, ExamResultEntity::class, LaboratoryEntity::class, FileAttachmentEntity::class],
    version = 1,
)
@TypeConverters(Converters::class)
abstract class AuraDatabase : RoomDatabase() {
    abstract fun examDao(): ExamDao
    abstract fun laboratoryDao(): LaboratoryDao
}
