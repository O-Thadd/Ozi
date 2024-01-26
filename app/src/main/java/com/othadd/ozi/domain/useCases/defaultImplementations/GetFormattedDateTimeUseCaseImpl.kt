package com.othadd.ozi.domain.useCases.defaultImplementations

import android.icu.util.Calendar
import com.othadd.ozi.domain.useCases.interfaces.GetFormattedDateTimeUseCase
import java.text.SimpleDateFormat
import java.util.Date

class GetFormattedDateTimeUseCaseImpl : GetFormattedDateTimeUseCase {
    override operator fun invoke(timeInMillis: Long, type: DateTimeFormatType): String{
        return when(type){
            DateTimeFormatType.FOR_CHAT_LIST -> formatForChatList(timeInMillis)
        }
    }

    private fun formatForChatList(timeInMillis: Long): String{
        val currentCalendar = Calendar.getInstance()
        val subjectCalendar = Calendar.getInstance().apply { setTimeInMillis(timeInMillis) }
        val currentDate = getDate(currentCalendar)
        val subjectDate = getDate(subjectCalendar)

        if (currentDate == subjectDate){
            return getFormattedDateWithPattern("h:mm a", subjectCalendar.time)
        }

        val yesterdayCalendar = Calendar.getInstance().apply {
            add(Calendar.DATE, -1)
        }

        if (getDate(yesterdayCalendar) == subjectDate){
            return  "yesterday"
        }

        return getFormattedDateWithPattern("dd/MM/yyyy", subjectCalendar.time)
    }

    private fun getDate(calendar: Calendar): Triple<Int, Int, Int>{
        return with(calendar){ Triple(this.get(Calendar.DATE), this.get(Calendar.MONTH), this.get(Calendar.YEAR)) }
    }

    private fun getFormattedDateWithPattern(pattern: String, date: Date): String{
        val format: SimpleDateFormat = SimpleDateFormat(pattern)
        return format.format(date)
    }
}

enum class DateTimeFormatType{
    FOR_CHAT_LIST
}