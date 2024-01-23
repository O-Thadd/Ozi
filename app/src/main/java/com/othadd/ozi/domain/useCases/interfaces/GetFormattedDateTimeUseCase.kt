package com.othadd.ozi.domain.useCases.interfaces

import com.othadd.ozi.domain.useCases.implementations.DateTimeFormatType

interface GetFormattedDateTimeUseCase {
    operator fun invoke(timeInMillis: Long, type: DateTimeFormatType): String
}