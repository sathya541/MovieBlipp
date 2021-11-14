package sathya.com.movieblipp.data.model

/**
 * Data & UI State Management
 */
sealed class UiState<Any> {

    class Loading<Any> : UiState<Any>()

    data class Error<Any>(val message: String) : UiState<Any>()

    data class Success<Any>(val data: Any) : UiState<Any>()

    companion object {

        /**
         * Returns [UiState.Loading] instance
         */
        fun <Any> loading() = Loading<Any>()

        /**
         * Returns [UiState.Error] instance.
         * @param message description of failure.
         */
        fun <Any> error(message: String) = Error<Any>(message)

        /**
         * Returns [UiState.Success] instance
         * @param data Data to emit with Status
         */
        fun <Any> success(data: Any) = Success<Any>(data)

    }
}