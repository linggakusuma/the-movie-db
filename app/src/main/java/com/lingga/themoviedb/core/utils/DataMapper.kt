package com.lingga.themoviedb.core.utils

import com.lingga.themoviedb.core.data.source.local.entity.MovieEntity
import com.lingga.themoviedb.core.data.source.local.entity.TvShowEntity
import com.lingga.themoviedb.core.data.source.remote.response.movie.MovieResponse
import com.lingga.themoviedb.core.data.source.remote.response.tvshow.TvShowResponse
import com.lingga.themoviedb.core.domain.model.Movie
import com.lingga.themoviedb.core.domain.model.TvShow

object DataMapper {

    fun responseToEntitiesMovie(input: List<MovieResponse>): List<MovieEntity> {
        val movieList = ArrayList<MovieEntity>()
        input.map {
            val movie = MovieEntity(
                id = it.id ?: 0,
                backdropPath = it.backdropPath,
                isFavorite = false,
                overview = it.overview,
                posterPath = it.posterPath,
                releaseDate = it.releaseDate,
                title = it.title,
                voteAverage = it.voteAverage,
                popularity = it.popularity
            )
            movieList.add(movie)
        }
        return movieList
    }

    fun mapEntitiesToDomainMovie(input: List<MovieEntity>): List<Movie> =
        input.map {
            Movie(
                id = it.id,
                voteAverage = it.voteAverage,
                title = it.title,
                releaseDate = it.releaseDate,
                posterPath = it.posterPath,
                overview = it.overview,
                isFavorite = it.isFavorite,
                backdropPath = it.backdropPath,
                popularity = it.popularity
            )
        }

    fun domainToEntityMovie(input: Movie) = MovieEntity(
        id = input.id ?: 0,
        backdropPath = input.backdropPath,
        overview = input.overview,
        posterPath = input.posterPath,
        releaseDate = input.releaseDate,
        title = input.title,
        voteAverage = input.voteAverage,
        isFavorite = input.isFavorite,
        popularity = input.popularity
    )

    fun responseToEntitiesTvShow(input: List<TvShowResponse>): List<TvShowEntity> =
        input.map {
            TvShowEntity(
                id = it.id ?: 0,
                voteAverage = it.voteAverage,
                isFavorite = false,
                posterPath = it.posterPath,
                overview = it.overview,
                firstAirDate = it.firsAirDate,
                backdropPath = it.backdropPath,
                name = it.name
            )
        }


    fun mapEntitiesToDomainTvShow(input: List<TvShowEntity>): List<TvShow> =
        input.map {
            TvShow(
                id = it.id,
                name = it.name,
                backdropPath = it.backdropPath,
                firstAirDate = it.firstAirDate,
                overview = it.overview,
                posterPath = it.posterPath,
                isFavorite = it.isFavorite,
                voteAverage = it.voteAverage
            )
        }

    fun responseToEntityMovie(input: MovieResponse): MovieEntity {
        return MovieEntity(
            id = input.id ?: 0,
            voteAverage = input.voteAverage,
            isFavorite = false,
            posterPath = input.posterPath,
            overview = input.overview,
            backdropPath = input.backdropPath,
            title = input.title,
            releaseDate = input.releaseDate,
            popularity = input.popularity
        )
    }

    fun entityToDomainMovie(input: MovieEntity) = Movie(
        id = input.id,
        releaseDate = input.releaseDate,
        title = input.title,
        backdropPath = input.backdropPath,
        overview = input.overview,
        posterPath = input.posterPath,
        isFavorite = input.isFavorite,
        voteAverage = input.voteAverage,
        popularity = input.popularity
    )

    fun responseToEntityTvShow(input: TvShowResponse) = TvShowEntity(
        id = input.id ?: 0,
        voteAverage = input.voteAverage,
        isFavorite = false,
        posterPath = input.posterPath,
        overview = input.overview,
        backdropPath = input.backdropPath,
        firstAirDate = input.firsAirDate,
        name = input.name
    )

    fun entityToDomainTvShow(input: TvShowEntity) = TvShow(
        id = input.id,
        name = input.name,
        firstAirDate = input.firstAirDate,
        backdropPath = input.backdropPath,
        overview = input.overview,
        posterPath = input.posterPath,
        isFavorite = input.isFavorite,
        voteAverage = input.voteAverage
    )

    fun responseToDomainMovie(input: MovieResponse) = Movie(
        id = input.id,
        genres = input.genres,
        popularity = input.popularity,
        voteAverage = input.voteAverage,
        isFavorite = false,
        posterPath = input.posterPath,
        overview = input.overview,
        backdropPath = input.backdropPath,
        title = input.title,
        releaseDate = input.releaseDate
    )

    fun responseToDomainTvShow(input: TvShowResponse) = TvShow(
        id = input.id,
        genres = input.genres,
        backdropPath = input.backdropPath,
        overview = input.overview,
        posterPath = input.posterPath,
        isFavorite = false,
        voteAverage = input.voteAverage,
        firstAirDate = input.firsAirDate,
        name = input.name
    )
}