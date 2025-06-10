package com.iyke.ozix.domain.useCases.interfaces

import com.iyke.ozix.domain.useCases.defaultImplementations.DateTimeFormatType

interface GetFormattedDateTimeUseCase {
    operator fun invoke(timeInMillis: Long, type: DateTimeFormatType): String
}