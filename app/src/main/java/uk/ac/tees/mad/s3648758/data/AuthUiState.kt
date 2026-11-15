package uk.ac.tees.mad.s3648758.data

data class AuthUiState(
    val loading: Boolean = false,
    val error: String? = null,
    val loggedInUser: String? = null
)
