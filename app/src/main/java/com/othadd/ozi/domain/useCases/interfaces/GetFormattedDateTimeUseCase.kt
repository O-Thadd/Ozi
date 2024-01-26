package com.othadd.ozi.domain.useCases.interfaces

import com.othadd.ozi.domain.useCases.defaultImplementations.DateTimeFormatType

interface GetFormattedDateTimeUseCase {
    operator fun invoke(timeInMillis: Long, type: DateTimeFormatType): String
}