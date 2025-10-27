package com.example.aura.domain.usecase

import com.example.aura.domain.repository.SampleRepository

class SampleUseCase(private val repository: SampleRepository) {
    operator fun invoke(): String = repository.ping()
}

