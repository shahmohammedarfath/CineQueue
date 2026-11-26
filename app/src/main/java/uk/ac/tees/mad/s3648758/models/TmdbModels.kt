package uk.ac.tees.mad.s3648758.models

import com.google.gson.annotations.SerializedName

data class TrendingResponse(
    val results: List<TmdbTitle>?
)

data class TmdbTitle(
    val id: Int,
    @SerializedName("media_type")
    val mediaType: String?,
    val title: String?,
    val name: String?,
    val overview: String?,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("release_date")
    val releaseDate: String?,
    @SerializedName("first_air_date")
    val firstAirDate: String?,
    @SerializedName("vote_average")
    val voteAverage: Double?
) {
    val displayTitle: String
        get() = title ?: name ?: "Untitled"

    val year: String
        get() = (releaseDate ?: firstAirDate)?.take(4) ?: ""

    val posterUrl: String?
        get() = posterPath?.let { "https://image.tmdb.org/t/p/w185$it" }
}

data class TitleDetails(
    val id: Int,
    val title: String?,
    val name: String?,
    val tagline: String?,
    val overview: String?,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("release_date")
    val releaseDate: String?,
    @SerializedName("first_air_date")
    val firstAirDate: String?,
    @SerializedName("vote_average")
    val voteAverage: Double?,
    // For movies only
    val runtime: Int?,
    // For TV Series only
    @SerializedName("number_of_seasons")
    val numberOfSeasons: Int?,
    val genres: List<Genre>?
) {
    val displayTitle: String
        get() = title ?: name ?: "Untitled"

    val year: String
        get() = (releaseDate ?: firstAirDate)?.take(4) ?: ""

    val posterUrl: String?
        get() = posterPath?.let { "https://image.tmdb.org/t/p/w500$it" }

    val genreText: String
        get() = genres.orEmpty().joinToString(", ") { it.name }
}

data class Genre(
    val id: Int,
    val name: String
)