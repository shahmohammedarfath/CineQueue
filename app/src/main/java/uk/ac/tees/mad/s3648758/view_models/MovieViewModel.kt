package uk.ac.tees.mad.s3648758.view_models


import androidx.lifecycle.ViewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import uk.ac.tees.mad.s3648758.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import uk.ac.tees.mad.s3648758.api.TmdbApiCall
import uk.ac.tees.mad.s3648758.models.TmdbTitle

class MovieViewModel : ViewModel(){

//    var titles by mutableStateOf<List<TmdbTitle>>(emptyList())
//        private set
//
//    var isLoading by mutableStateOf(false)
//        private set
//
//    var errorMessage by mutableStateOf<String?>(null)
//        private set
//
//    suspend fun loadTrending() {
//
//        if (titles.isNotEmpty()) return
//
//        isLoading = true
//        errorMessage = null
//
//        try {
//
//            val response = Retro
//        }
//    }

    private var _titlesDataFlow =
        MutableStateFlow<MutableList<TmdbTitle>>(mutableStateListOf())

    val titlesDataFlow: StateFlow<List<TmdbTitle>>
        get() = _titlesDataFlow

    private var _isLoadingFlow = MutableStateFlow(false)
    val isLoadingFlow: StateFlow<Boolean>
        get() = _isLoadingFlow

    private var _errorMessageFlow = MutableStateFlow<String?>(null)
    val errorMessageFlow: StateFlow<String?>
        get() = _errorMessageFlow

    init {
        fetchTrending()
    }

    fun fetchTrending(apiKey: String = BuildConfig.TMDB_API_KEY) {

        if (_titlesDataFlow.value.isNotEmpty()) return

        _isLoadingFlow.value = true
        _errorMessageFlow.value = null

        viewModelScope.launch(Dispatchers.IO) {
            TmdbApiCall().getTrending(
                apiKey = apiKey,
                onSuccess = {trendingResponse ->
                    _titlesDataFlow.value.clear()
                    _titlesDataFlow.value.addAll(trendingResponse.results.orEmpty())
                    _isLoadingFlow.value = false
                },

                onError = { message ->
                    _errorMessageFlow.value = message
                    _isLoadingFlow.value = false
                }
            )
        }
    }


}