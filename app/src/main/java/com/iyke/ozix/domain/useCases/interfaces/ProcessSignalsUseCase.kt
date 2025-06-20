package com.iyke.ozix.domain.useCases.interfaces

interface ProcessSignalsUseCase {
    suspend operator fun invoke()
    suspend fun refreshMessages()
}