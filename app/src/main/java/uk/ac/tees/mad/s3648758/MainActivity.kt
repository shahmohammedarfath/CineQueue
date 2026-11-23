package uk.ac.tees.mad.s3648758

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import uk.ac.tees.mad.s3648758.screens.HomeScreen
//import uk.ac.tees.mad.s3648758.screens.LandingScreen
import uk.ac.tees.mad.s3648758.screens.LoginScreen
import uk.ac.tees.mad.s3648758.ui.theme.CineQueueTheme
//import uk.ac.tees.mad.s3648758.view_models.MovieViewModel
import uk.ac.tees.mad.s3648758.view_models.UserViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CineQueueTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    App(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun App(modifier: Modifier = Modifier) {

    Column(modifier = modifier) {

        val navController = rememberNavController()

        val userViewModel: UserViewModel = viewModel()

//        val movieViewModel: MovieViewModel = viewModel()

        NavHost(
            navController = navController,

            startDestination = if (userViewModel.isLoggedIn) "landing" else "login"
        ) {

            composable(route = "login") {
                LoginScreen(navController, userViewModel)
            }

//            composable(route = "landing") {
//                LandingScreen(navController, userViewModel, movieViewModel)
//            }

            composable(route = "home") {
                HomeScreen()
            }
        }
    }
}