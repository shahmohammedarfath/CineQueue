package uk.ac.tees.mad.s3648758.view_models

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.core.text.trimmedLength
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.tasks.await

class UserViewModel: ViewModel() {

    private val auth = FirebaseAuth.getInstance()

    var isLoggedIn by mutableStateOf(auth.currentUser != null)
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var username by mutableStateOf(
        auth.currentUser?.email?.substringBefore("@") ?: ""
    )
        private set

    private fun toEmail(name: String) = "${name.trim().lowercase()}@cinequeue.app"

    private fun validate(name: String, password: String): String? {
        if (!Regex("[A-Za-z0-9_.]{3,20}$").matches(name.trim()))
            return "Username must be 3-20 characters: letters, numbers, . or _"
        if(password.length < 8)
            return "Password must be at least 8 characters"
        return null
    }

//    fun submit(username: String, password: String,isRegister: Boolean) {
//        validate(username, password)?.let {
//            uiState = uiState.copy(error = it)
//            return
//        }
//
//        uiState = uiState.copy(loading = true, error = null)
//
//        val email = toEmail(username)
//        val task = if (isRegister)
//            auth.createUserWithEmailAndPassword(email, password)
//
//        else
//            auth.signInWithEmailAndPassword(email, password)
//
//        task.addOnCompleteListener { result ->
//            uiState = if (result.isSuccessful) {
//                AuthUiState(loggedInUser = username.trim().lowercase())
//            } else {
//                AuthUiState(error = friendlyError(result.exception))
//            }
//        }
//    }

    suspend fun doAuth(name: String, password: String, isRegister: Boolean): Boolean {

        val problem = validate(name, password)
        if (problem != null) {
            errorMessage = problem
            return false
        }

        isLoading = true
        errorMessage = null

        try {
            val email = toEmail(name)

            if (isRegister) {
                auth.createUserWithEmailAndPassword(email, password).await()
            } else {
                auth.signInWithEmailAndPassword(email, password).await()
            }

            username = name.trim().lowercase()
            isLoggedIn = true
            return true
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            errorMessage = friendlyError(e)
            return false
        } finally {
            isLoading = false
        }
    }

    private fun friendlyError(e: Exception?): String = when(e) {
        is FirebaseAuthUserCollisionException -> "That username is already taken"
        is FirebaseAuthInvalidUserException, is FirebaseAuthInvalidCredentialsException -> "Incorrect username or password"
        is FirebaseNetworkException -> "No internet connection"
        else -> "Something went wrong. Please try again"
    }

    fun clearError() {
        errorMessage = null
    }

    fun signOut() {
        auth.signOut()
        username = ""
        isLoggedIn = false
    }
}