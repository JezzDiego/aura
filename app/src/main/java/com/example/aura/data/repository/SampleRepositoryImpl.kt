package com.example.aura.data.repository

import com.example.aura.domain.repository.SampleRepository

class SampleRepositoryImpl : SampleRepository {
    override fun ping(): String = "pong"
}
