package uk.ac.tees.mad.s3648758.api

import android.util.Log
import okhttp3.Response
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.GET
import retrofit2.http.Query
import uk.ac.tees.mad.s3648758.models.TmdbModels

private val BASE_URL = "https://api.themoviedb.org/3/"

interface TmdbApi {

    @GET("trending/all/week")

    fun getTrending(
        @Query("api_key") apiKey: String
    ) : Call<TrendingResponse>
}

class TmdbApiCall() {

    suspend fun getTrending(
        apiKey: String,
        onSuccess: (TrendingResponse) -> Unit = {_ ->},
        onError: (message: String) -> Unit = {}
    ) {
        val retrofit: Retrofit =
            Retrofit
                .Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val service: TmdbApi =
            retrofit.create(TmdbApi::class.java)

        val call: Call<TrendingResponse> = service.getTrending(apiKey)

        call.enqueue(object : Callback<TrendingResponse> {

            override fun onResponse(
                call: Call<TrendingResponse>?,
                response: retrofit2.Response<TrendingResponse>?
            ) {
                response?.let {
                    if (response.isSuccessful) {
                        Log.d(
                            "package:mine",
                            response.raw().message
                        )

                        response.body()?.let {
                            onSuccess(it)
                        }
                    } else {
                        Log.e(
                            "Package:mine",
                            "Something went wrong - ${response.message()}"
                        )

                        onError("Something went wrong - ${response.message()}")
                    }
                }
            }

            override fun onFailure(call: Call<TrendingResponse?>, t: Throwable?) {
                Log.e(
                    "Package:mine",
                    "Failed to fetch data - ${t?.message ?: "Unknown error"}"
                )

                onError("Could not fetch data")
            }
        })
    }
}

