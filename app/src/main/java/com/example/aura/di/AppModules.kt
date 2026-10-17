package com.example.aura.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.aura.data.local.dao.EstablishmentDao
import com.example.aura.data.local.dao.ReviewDao
import com.example.aura.data.local.dao.UserDao
import com.example.aura.data.local.datasource.EstablishmentLocalDataSource
import com.example.aura.data.local.datasource.PhotoLocalDataSource
import com.example.aura.data.local.datasource.ReviewLocalDataSource
import com.example.aura.data.local.datasource.UserLocalDataSource
import com.example.aura.data.local.db.AuraDatabase
import com.example.aura.data.local.preferences.AuraPreferences
import com.example.aura.data.remote.api.EstablishmentApi
import com.example.aura.data.remote.api.ReviewApi
import com.example.aura.data.remote.api.UserApi
import com.example.aura.data.remote.datasource.EstablishmentRemoteDataSource
import com.example.aura.data.remote.datasource.ReviewRemoteDataSource
import com.example.aura.data.remote.datasource.UserRemoteDataSource
import com.example.aura.data.remote.network.RetrofitClient
import com.example.aura.data.repository.EstablishmentRepositoryImpl
import com.example.aura.data.repository.ReviewRepositoryImpl
import com.example.aura.data.repository.UserRepositoryImpl
import com.example.aura.domain.repository.EstablishmentRepository
import com.example.aura.domain.repository.ReviewRepository
import com.example.aura.domain.repository.UserRepository
import com.example.aura.domain.usecase.establishment.*
import com.example.aura.domain.usecase.review.*
import com.example.aura.domain.usecase.user.*
import retrofit2.Retrofit

/**
 * Manual DI container. Instantiate once (e.g., in Application) and access its singletons.
 */
class AppContainer(context: Context, app: Application) {
    val application = app

    // Core singletons
    // Retrofit é criado de forma lazy e só será instanciado quando a API remota for ligada.
    private val retrofit: Retrofit by lazy { RetrofitClient.instance }

    private val database: AuraDatabase by lazy {
        Room.databaseBuilder(context.applicationContext, AuraDatabase::class.java, "aura.db")
            .fallbackToDestructiveMigration(true)
            .build()
    }

    // DAOs
    private val establishmentDao: EstablishmentDao by lazy { database.establishmentDao() }
    private val reviewDao: ReviewDao by lazy { database.reviewDao() }
    private val userDao: UserDao by lazy { database.userDao() }

    // APIs (não utilizadas enquanto o app é offline)
    private val establishmentApi: EstablishmentApi by lazy { retrofit.create(EstablishmentApi::class.java) }
    private val reviewApi: ReviewApi by lazy { retrofit.create(ReviewApi::class.java) }
    private val userApi: UserApi by lazy { retrofit.create(UserApi::class.java) }

    // Data sources

    // local
    val establishmentLocalDataSource by lazy { EstablishmentLocalDataSource(establishmentDao) }
    val reviewLocalDataSource by lazy { ReviewLocalDataSource(reviewDao) }
    val userLocalDataSource by lazy { UserLocalDataSource(userDao) }
    val photoLocalDataSource by lazy { PhotoLocalDataSource(context.applicationContext) }

    // remote
    val establishmentRemoteDataSource by lazy { EstablishmentRemoteDataSource(establishmentApi) }
    val reviewRemoteDataSource by lazy { ReviewRemoteDataSource(reviewApi) }
    val userRemoteDataSource by lazy { UserRemoteDataSource(userApi) }

    // Repositories
    val establishmentRepository: EstablishmentRepository by lazy {
        EstablishmentRepositoryImpl(
            localDS = establishmentLocalDataSource,
            remoteDS = establishmentRemoteDataSource,
        )
    }
    val reviewRepository: ReviewRepository by lazy {
        ReviewRepositoryImpl(
            localDS = reviewLocalDataSource,
            photoDS = photoLocalDataSource,
            remoteDS = reviewRemoteDataSource,
        )
    }
    val userRepository: UserRepository by lazy {
        UserRepositoryImpl(
            localDS = userLocalDataSource,
            remoteDS = userRemoteDataSource,
        )
    }

    // Use cases
    val establishmentUseCases: EstablishmentUseCases by lazy {
        EstablishmentUseCases(
            getEstablishments = GetEstablishmentsUseCase(establishmentRepository),
            getEstablishmentById = GetEstablishmentByIdUseCase(establishmentRepository),
            searchEstablishments = SearchEstablishmentsUseCase(establishmentRepository),
            getFavorites = GetFavoriteEstablishmentsUseCase(establishmentRepository),
            toggleFavorite = ToggleFavoriteUseCase(establishmentRepository),
            shareEstablishment = ShareEstablishmentUseCase(establishmentRepository),
        )
    }

    val reviewUseCases: ReviewUseCases by lazy {
        ReviewUseCases(
            getRecentReviews = GetRecentReviewsUseCase(reviewRepository),
            getEstablishmentReviews = GetEstablishmentReviewsUseCase(reviewRepository),
            getUserReviews = GetUserReviewsUseCase(reviewRepository),
            submitReview = SubmitReviewUseCase(reviewRepository, establishmentRepository),
            toggleHelpful = ToggleReviewHelpfulUseCase(reviewRepository),
            savePhoto = SaveReviewPhotoUseCase(reviewRepository),
            deletePhoto = DeleteReviewPhotoUseCase(reviewRepository),
        )
    }

    val userUseCases: UserUseCases by lazy {
        UserUseCases(
            loginUser = LoginUserUseCase(userRepository),
            logoutUser = LogoutUserUseCase(userRepository),
            getLocalUser = GetLocalUserUseCase(userRepository),
            updateAccessibilityProfile = UpdateAccessibilityProfileUseCase(userRepository),
            updateUserName = UpdateUserNameUseCase(userRepository),
        )
    }

    // Preferences / DataStore
    val preferences: AuraPreferences by lazy { AuraPreferences(context.applicationContext) }
}
