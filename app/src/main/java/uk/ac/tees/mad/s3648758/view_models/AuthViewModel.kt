package uk.ac.tees.mad.s3648758.view_models

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import uk.ac.tees.mad.s3648758.data.AuthUiState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException

class AuthViewModel: ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    var uiState by mutableStateOf(
        AuthUiState(loggedInUser = auth.currentUser?.email?.substringBefore("@"))
    )

        private set

    private fun toEmail(username: String) = "${username.trim().lowercase()}@cinequeue.app"

    private fun validate(username: String, password: String): String? {
        if (!Regex("[A-Za-z0-9_.]{3,20}$").matches(username.trim()))
            return "Username must be 3-20 characters: letters, numbers, . or _"
        if(password.length < 8)
            return "Password must be at least 8 characters"
        return null
    }

    fun submit(username: String, password: String,isRegister: Boolean) {
        validate(username, password)?.let {
            uiState = uiState.copy(error = it)
            return
        }

        uiState = uiState.copy(loading = true, error = null)

        val email = toEmail(username)
        val task = if (isRegister)
            auth.createUserWithEmailAndPassword(email, password)

        else
            auth.signInWithEmailAndPassword(email, password)

        task.addOnCompleteListener { result ->
            uiState = if (result.isSuccessful) {
                AuthUiState(loggedInUser = username.trim().lowercase())
            } else {
                AuthUiState(error = friendlyError(result.exception))
            }
        }
    }

    private fun friendlyError(e: Exception?): String = when(e) {
        is FirebaseAuthUserCollisionException -> "That username is already taken"
        is FirebaseAuthInvalidUserException, is FirebaseAuthInvalidCredentialsException -> "Incorrect username or password"
        is FirebaseNetworkException -> "No internet connection"
        else -> "Something went wrong. Please try again"
    }

    fun clearError() {
        uiState = uiState.copy(error = null)
    }

    fun signOut() {
        auth.signOut()
        uiState = AuthUiState()
    }
}