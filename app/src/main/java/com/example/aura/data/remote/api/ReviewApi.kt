package com.example.aura.data.remote.api

import com.example.aura.data.remote.dto.ReviewDTO
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ReviewApi {

    @GET("reviews")
    suspend fun getRecentReviews(@Query("limit") limit: Int): List<ReviewDTO>

    @GET("establishments/{id}/reviews")
    suspend fun getReviewsByEstablishment(@Path("id") establishmentId: String): List<ReviewDTO>

    @POST("reviews")
    suspend fun publishReview(@Body review: ReviewDTO)

    @POST("reviews/{id}/helpful")
    suspend fun markHelpful(@Path("id") reviewId: String)
}
