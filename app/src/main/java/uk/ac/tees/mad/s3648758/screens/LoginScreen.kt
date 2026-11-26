package uk.ac.tees.mad.s3648758.screens

import androidx.compose.material3.Button
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import kotlinx.coroutines.*
import uk.ac.tees.mad.s3648758.view_models.UserViewModel


@Composable
fun LoginScreen(
//    state: AuthUiState,
//    onSubmit: (username: String, password: String, isRegister: Boolean) -> Unit,
//    onInputChanged: () -> Unit

    navHostController: NavHostController,
    userViewModel: UserViewModel,
    modifier: Modifier = Modifier

    ) {

    val coroutineScope = rememberCoroutineScope()

    var username by rememberSaveable { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isRegister by rememberSaveable { mutableStateOf(false) }
    var showPassword by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("CineQueue", style = MaterialTheme.typography.headlineLarge)
        Spacer(Modifier.height(4.dp))
        Text(
            if(isRegister) "Create your account" else "Welcome back",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(Modifier.height(24.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it; userViewModel.clearError()},
            label = { Text("Username")},
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it; userViewModel.clearError()},
            label = { Text("Password") },
            singleLine = true,
            visualTransformation =
                if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            trailingIcon = {
                TextButton(onClick = { showPassword = !showPassword}) {
                    Text(if (showPassword) "Hide" else "Show")
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

//        state.error?.let {
//            Spacer(Modifier.height(12.dp))
//            Text(it, color = MaterialTheme.colorScheme.error)
//        }

        if (userViewModel.errorMessage != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = userViewModel.errorMessage ?: "",
                color = MaterialTheme.colorScheme.error
            )
        }

        Spacer(Modifier.height(20.dp))
        Button(
            enabled = !userViewModel.isLoading,
            onClick = {
                coroutineScope.launch {
                    val success = userViewModel.doAuth(username, password, isRegister)

                    if (success) {
                        navHostController.navigate("landing") {

                            popUpTo("login") {inclusive = true}
                        }
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            if (userViewModel.isLoading) {
                CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp)
            } else {
                Text(if (isRegister) "Create account" else "Log in")
            }
        }

//        TextButton(onClick = { isRegister = !isRegister; onInputChanged}) {
//            Text(
//                if (isRegister) "Already have an account? Log in"
//                else "New here? Create an account"
//            )
//        }

        TextButton(
            onClick = {
                isRegister = !isRegister
                userViewModel.clearError()
            }
        ) {
            Text(
                text = if (isRegister) "Already have an account? Log in"
                else "New here? Create an account"
            )
        }
    }
}