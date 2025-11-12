package com.example.aura.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.aura.data.local.dao.ArticleDao
import com.example.aura.data.local.dao.ExamDao
import com.example.aura.data.local.dao.LaboratoryDao
import com.example.aura.data.local.dao.MedicationDao
import com.example.aura.data.local.dao.UserDao
import com.example.aura.data.local.datasource.ExamLocalDataSource
import com.example.aura.data.local.datasource.LaboratoryLocalDataSource
import com.example.aura.data.local.datasource.MedicationLocalDataSource
import com.example.aura.data.local.datasource.UserLocalDataSource
import com.example.aura.data.local.db.AuraDatabase
import com.example.aura.data.local.preferences.AuraPreferences
import com.example.aura.data.remote.api.ArticlesApi
import com.example.aura.data.remote.api.ExamApi
import com.example.aura.data.remote.api.LaboratoryApi
import com.example.aura.data.remote.api.MedicationApi
import com.example.aura.data.remote.api.UserApi
import com.example.aura.data.remote.datasource.ExamRemoteDataSource
import com.example.aura.data.remote.datasource.LaboratoryRemoteDataSource
import com.example.aura.data.remote.datasource.MedicationRemoteDataSource
import com.example.aura.data.remote.datasource.UserRemoteDataSource
import com.example.aura.data.remote.network.RetrofitClient
import com.example.aura.data.repository.ArticleRepositoryImpl
import com.example.aura.data.repository.ExamRepositoryImpl
import com.example.aura.data.repository.LaboratoryRepositoryImpl
import com.example.aura.data.repository.MedicationRepositoryImpl
import com.example.aura.data.repository.UserRepositoryImpl
import com.example.aura.domain.repository.ArticleRepository
import com.example.aura.domain.repository.ExamRepository
import com.example.aura.domain.repository.LaboratoryRepository
import com.example.aura.domain.repository.UserRepository
import com.example.aura.domain.usecase.article.ArticleUseCases
import com.example.aura.domain.usecase.article.GetArticleByIdUseCase
import com.example.aura.domain.usecase.article.GetArticleListUseCaseAPI
import com.example.aura.domain.usecase.article.GetArticleListUseCaseDAO
import com.example.aura.domain.usecase.article.IsArticleSavedUseCase
import com.example.aura.domain.usecase.article.ToggleArticleSavedStatusUseCase
import com.example.aura.domain.usecase.exam.*
import com.example.aura.domain.usecase.laboratory.*
import com.example.aura.domain.usecase.medication.AddMedicationUseCase
import com.example.aura.domain.usecase.medication.DeleteMedicationUseCase
import com.example.aura.domain.usecase.medication.GetSavedMedicationsUseCase
import com.example.aura.domain.usecase.medication.MedicationUseCases
import com.example.aura.domain.usecase.medication.SearchMedicationsUseCase
import com.example.aura.domain.usecase.user.*
import retrofit2.Retrofit
import kotlin.getValue

/**
 * Manual DI container. Instantiate once (e.g., in Application) and access its singletons.
 */
class AppContainer(context: Context, app: Application) {
    val application = app

    // Core singletons
    private val retrofit: Retrofit by lazy { RetrofitClient.instance }
    private val retrofit2: Retrofit by lazy { RetrofitClient.instance2 }

    private val database: AuraDatabase by lazy {
        Room.databaseBuilder(context.applicationContext, AuraDatabase::class.java, "aura.db")
            // .addMigrations(MIGRATION_1_2, ...)
            .fallbackToDestructiveMigration(true)
            .build()
    }

    // DAOs
    private val examDao: ExamDao by lazy { database.examDao() }
    private val laboratoryDao: LaboratoryDao by lazy { database.laboratoryDao() }
    val userDao: UserDao by lazy { database.userDao() }
    private val articleDao: ArticleDao by lazy {database.articleDao()}
    private val medicationDao: MedicationDao by lazy { database.medicationDao() }

    // APIs
    private val examApi: ExamApi by lazy { retrofit.create(ExamApi::class.java) }
    private val laboratoryApi: LaboratoryApi by lazy { retrofit.create(LaboratoryApi::class.java) }
    private val userApi: UserApi by lazy { retrofit.create(UserApi::class.java) }
    private val articleApi: ArticlesApi by lazy {retrofit2.create(ArticlesApi::class.java)}
    private val medicationApi: MedicationApi by lazy { retrofit.create(MedicationApi::class.java) }

    // Data sources

    // local
    val examLocalDataSource: ExamLocalDataSource by lazy { ExamLocalDataSource(examDao) }
    val laboratoryLocalDataSource: LaboratoryLocalDataSource by lazy { LaboratoryLocalDataSource(laboratoryDao) }
    val userLocalDataSource: UserLocalDataSource by lazy { UserLocalDataSource(userDao) }
    val medicationLocalDataSource: MedicationLocalDataSource by lazy { MedicationLocalDataSource(medicationDao) }

    // remote
    val examRemoteDataSource: ExamRemoteDataSource by lazy { ExamRemoteDataSource(examApi) }
    val laboratoryRemoteDataSource: LaboratoryRemoteDataSource by lazy { LaboratoryRemoteDataSource(laboratoryApi) }
    val userRemoteDataSource: UserRemoteDataSource by lazy { UserRemoteDataSource(userApi) }
    val medicationRemoteDataSource: MedicationRemoteDataSource by lazy { MedicationRemoteDataSource(medicationApi) }

    // Repositories
    val examRepository: ExamRepository by lazy {
        ExamRepositoryImpl(
            localDS = examLocalDataSource,
            remoteDS = examRemoteDataSource
        )
    }
    val laboratoryRepository: LaboratoryRepository by lazy {
        LaboratoryRepositoryImpl(
            localDS = laboratoryLocalDataSource,
            remoteDS = laboratoryRemoteDataSource
        )
    }
    val userRepository: UserRepository by lazy {
        UserRepositoryImpl(
            localDS = userLocalDataSource,
            remoteDS = userRemoteDataSource
        )
    }
    val articleRepository: ArticleRepository by lazy {
        ArticleRepositoryImpl(
            api = articleApi,
            dao = articleDao
        )
    }
    val medicationRepository: MedicationRepositoryImpl by lazy {
        MedicationRepositoryImpl(
            localDataSource = medicationLocalDataSource,
            remoteDataSource = medicationRemoteDataSource
        )
    }

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
            loginUser = LoginUserUseCase(userRepository),
            getAllUsers = GetUserListUseCase(userRepository),
            getLocalUser = GetLocalUserUseCase(userRepository),
            logoutUser = LogoutUserUseCase(userRepository)
        )
    }

    val articleUserCases: ArticleUseCases by lazy{
        ArticleUseCases(
            getArticleListAPI = GetArticleListUseCaseAPI(articleRepository),
            getArticlesDAO = GetArticleListUseCaseDAO(articleRepository),
            getArticleById = GetArticleByIdUseCase(articleRepository),
            toggleArticleSavedStatus = ToggleArticleSavedStatusUseCase(articleRepository),
            isArticleSaved = IsArticleSavedUseCase(articleRepository)
        )
    }

    val medicationUseCases: MedicationUseCases by lazy {
        MedicationUseCases(
            getSavedMedications = GetSavedMedicationsUseCase(medicationRepository),
            addMedication = AddMedicationUseCase(medicationRepository),
            deleteMedication = DeleteMedicationUseCase(medicationRepository),
            searchMedications = SearchMedicationsUseCase(medicationRepository)
        )
    }

    // Preferences / DataStore
    val preferences: AuraPreferences by lazy { AuraPreferences(context.applicationContext) }
}
