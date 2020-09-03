package com.data.source.local.room

import androidx.paging.DataSource
import androidx.room.*
import com.data.source.local.entity.TvShowEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TvShowDao {

    @Query("select * from tv_show")
    fun getAllTvShow(): Flow<List<TvShowEntity>>

    @Query("select * from tv_show where isFavorite = 1")
    fun getFavoriteTvShow(): DataSource.Factory<Int, TvShowEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTvShow(tvShow: List<TvShowEntity>)

    @Update
    fun updateFavoriteTvShow(tvShow: TvShowEntity)

}