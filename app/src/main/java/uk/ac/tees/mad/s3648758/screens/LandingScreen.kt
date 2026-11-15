package uk.ac.tees.mad.s3648758.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import uk.ac.tees.mad.s3648758.view_models.UserViewModel

@Composable
fun LandingScreen(
    navHostController: NavHostController,
    userViewModel: UserViewModel,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Welcome, ${userViewModel.username}")

        Button(
            onClick = {
                userViewModel.signOut()
                navHostController.navigate("login") {
                    popUpTo("landing") { inclusive = true }
                }
            }
        ) {
            Text(text = "Log out")
        }
    }
}