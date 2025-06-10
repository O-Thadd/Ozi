package com.iyke.ozix.domain.model

import com.iyke.ozix.data.dataSources.model.OpError

/**
 * [T] is the type of the expected data if operation is meant to return a result.
 * If a result is not expected i.e. returns [Unit], [Nothing] suffices.
 *
 * [U] is the type of the expected cause for failure of the operation. Usually [ServerResponseStatus.FAILURE].
 * If the particular cause of the failure is not useful i.e. just knowing that the operation failed is enough,
 * then [Nothing] suffices
 */
sealed class OperationOutcome<T, U> {
    class Successful<T, U>(val data: T? = null) : OperationOutcome<T, U>()

    class Failed<T, U>(val cause: U? = null) : OperationOutcome<T, U>()
}

sealed class OperationOutcomeX<out T, out U: OpError> {
    class Successful<T>(val data: T? = null) : OperationOutcomeX<T, Nothing>()

    class Failed<U: OpError>(val cause: U? = null) : OperationOutcomeX<Nothing, U>()
}