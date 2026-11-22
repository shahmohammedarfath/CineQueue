package uk.ac.tees.mad.s3648758.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import uk.ac.tees.mad.s3648758.view_models.MovieViewModel
import uk.ac.tees.mad.s3648758.view_models.UserViewModel
import java.nio.file.WatchEvent

@Composable
fun LandingScreen(
    navHostController: NavHostController,
    userViewModel: UserViewModel,
    movieViewModel: MovieViewModel,
    modifier: Modifier = Modifier
) {
//    Column(
//        modifier = Modifier.fillMaxSize(),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Text(text = "Welcome, ${userViewModel.username}")
//
//        Button(
//            onClick = {
//                userViewModel.signOut()
//                navHostController.navigate("login") {
//                    popUpTo("landing") { inclusive = true }
//                }
//            }
//        ) {
//            Text(text = "Log out")
//        }
//    }

    val titles by movieViewModel.titlesDataFlow.collectAsState()
    val isLoading by movieViewModel.isLoadingFlow.collectAsState()
    val errorMessage by movieViewModel.errorMessageFlow.collectAsState()

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {

        // Top row: greeting on the left, log out on the right.
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Hi, ${userViewModel.username}",
                style = MaterialTheme.typography.titleMedium
            )
            TextButton(
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

        Text(
            text = "Trending this week",
            style = MaterialTheme.typography.headlineSmall
        )

        Spacer(modifier = Modifier.height(8.dp))

        // The list takes the remaining space; the TMDB notice sits below it.
        Box(modifier = Modifier.weight(1f).fillMaxWidth()) {

            when {
                // 1. Still downloading.
                isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                // 2. Something went wrong: show the message and a retry button.
                errorMessage != null -> {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = errorMessage ?: "",
                            color = MaterialTheme.colorScheme.error
                        )
                        Button(
                            onClick = {
                                movieViewModel.fetchTrending()
                            }
                        ) {
                            Text(text = "Try again")
                        }
                    }
                }

                // 3. Success: show the list. LazyColumn only draws the rows
                // that are visible, which is kind to memory and battery.
                else -> {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        items(
                            items = titles,
                            // id alone is not unique: a movie and a TV series
                            // can share the same number.
                            key = { "${it.mediaType}-${it.id}" }
                        ) { title ->
                            Row(modifier = Modifier.fillMaxWidth()) {

                                // Coil downloads and caches the poster image.
                                AsyncImage(
                                    model = title.posterUrl,
                                    contentDescription = title.displayTitle,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier.width(80.dp).height(120.dp)
                                )

                                Column(modifier = Modifier.padding(start = 12.dp)) {
                                    Text(
                                        text = title.displayTitle,
                                        style = MaterialTheme.typography.titleMedium,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = listOf(
                                            title.mediaType?.replaceFirstChar { it.uppercase() },
                                            title.year
                                        ).filter { !it.isNullOrBlank() }.joinToString(" • "),
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                    title.voteAverage?.let {
                                        Text(
                                            text = "★ ${"%.1f".format(it)}",
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Attribution that TMDB requires for using its data.
        Text(
            text = "This product uses the TMDB API but is not endorsed or certified by TMDB.",
            style = MaterialTheme.typography.labelSmall
        )
    }

}