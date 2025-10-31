package com.example.aura.di

import android.content.Context
import androidx.room.Room
import com.example.aura.data.local.dao.ExamDao
import com.example.aura.data.local.dao.LaboratoryDao
import com.example.aura.data.local.datasource.ExamLocalDataSource
import com.example.aura.data.local.datasource.LaboratoryLocalDataSource
import com.example.aura.data.local.db.AuraDatabase
import com.example.aura.data.local.preferences.AuraPreferences
import com.example.aura.data.remote.api.ExamApi
import com.example.aura.data.remote.api.LaboratoryApi
import com.example.aura.data.remote.api.UserApi
import com.example.aura.data.remote.datasource.ExamRemoteDataSource
import com.example.aura.data.remote.datasource.LaboratoryRemoteDataSource
import com.example.aura.data.remote.datasource.UserRemoteDataSource
import com.example.aura.data.remote.network.RetrofitClient
import com.example.aura.data.repository.ExamRepositoryImpl
import com.example.aura.data.repository.LaboratoryRepositoryImpl
import com.example.aura.data.repository.UserRepositoryImpl
import com.example.aura.domain.repository.ExamRepository
import com.example.aura.domain.repository.LaboratoryRepository
import com.example.aura.domain.repository.UserRepository
import com.example.aura.domain.usecase.exam.*
import com.example.aura.domain.usecase.laboratory.*
import com.example.aura.domain.usecase.user.*
import retrofit2.Retrofit

/**
 * Manual DI container. Instantiate once (e.g., in Application) and access its singletons.
 */
class AppContainer(context: Context) {

    // Core singletons
    private val retrofit: Retrofit by lazy { RetrofitClient.instance }

    private val database: AuraDatabase by lazy {
        Room.databaseBuilder(context.applicationContext, AuraDatabase::class.java, "aura.db")
            // .addMigrations(MIGRATION_1_2, ...)
            .fallbackToDestructiveMigration(true)
            .build()
    }

    // DAOs
    private val examDao: ExamDao by lazy { database.examDao() }
    private val laboratoryDao: LaboratoryDao by lazy { database.laboratoryDao() }

    // APIs
    private val examApi: ExamApi by lazy { retrofit.create(ExamApi::class.java) }
    private val laboratoryApi: LaboratoryApi by lazy { retrofit.create(LaboratoryApi::class.java) }
    private val userApi: UserApi by lazy { retrofit.create(UserApi::class.java) }

    // Data sources
    val examLocalDataSource: ExamLocalDataSource by lazy { ExamLocalDataSource(examDao) }
    val laboratoryLocalDataSource: LaboratoryLocalDataSource by lazy { LaboratoryLocalDataSource(laboratoryDao) }
    val examRemoteDataSource: ExamRemoteDataSource by lazy { ExamRemoteDataSource(examApi) }
    val laboratoryRemoteDataSource: LaboratoryRemoteDataSource by lazy { LaboratoryRemoteDataSource(laboratoryApi) }
    val userRemoteDataSource: UserRemoteDataSource by lazy { UserRemoteDataSource(userApi) }

    // Repositories (currently depend directly on Api/Dao; ready for future refactor to use DS)
    val examRepository: ExamRepository by lazy { ExamRepositoryImpl(api = examApi, dao = examDao) }
    val laboratoryRepository: LaboratoryRepository by lazy { LaboratoryRepositoryImpl(api = laboratoryApi) }
    val userRepository: UserRepository by lazy { UserRepositoryImpl(api = userApi) }

    // Use cases
    val examUseCases: ExamUseCases by lazy {
        ExamUseCases(
            getExamList = GetExamListUseCase(examRepository),
            getExamById = GetExamByIdUseCase(examRepository),
            createExam = CreateExamUseCase(examRepository),
            deleteExam = DeleteExamUseCase(examRepository),
            shareExam = ShareExamUseCase(examRepository)
        )
    }

    val laboratoryUseCases: LaboratoryUseCases by lazy {
        LaboratoryUseCases(
            getLaboratories = GetLaboratoriesUseCase(laboratoryRepository),
            getById = GetLaboratoryByIdUseCase(laboratoryRepository),
            searchLaboratories = SearchLaboratoriesUseCase(laboratoryRepository)
        )
    }

    val userUseCases: UserUseCases by lazy {
        UserUseCases(
            getProfile = GetUserProfileUseCase(userRepository),
            updateProfile = UpdateUserProfileUseCase(userRepository),
            deleteAccount = DeleteAccountUseCase(userRepository),
            checkPremiumStatus = CheckPremiumStatusUseCase(userRepository)
        )
    }

    // Preferences / DataStore
    val preferences: AuraPreferences by lazy { AuraPreferences(context.applicationContext) }
}
