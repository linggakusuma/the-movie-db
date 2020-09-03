package com.data.source.repository

import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.data.NetworkBoundResource
import com.data.Resource
import com.data.source.local.TvShowLocalDataSource
import com.data.source.remote.TvShowRemoteDataSource
import com.data.source.remote.network.ApiResponse
import com.data.source.remote.response.tvshow.TvShowResponse
import com.domain.model.TvShow
import com.domain.repository.ITvShowRepository
import com.utils.AppExecutors
import com.utils.DataMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TvShowRepository @Inject constructor(
    private val remoteDataSource: TvShowRemoteDataSource,
    private val localDataSource: TvShowLocalDataSource,
    private val appExecutors: AppExecutors
) : ITvShowRepository {

    override fun getAllTvShow(): Flow<Resource<List<TvShow>>> =
        object : NetworkBoundResource<List<TvShow>, List<TvShowResponse>>() {
            override fun loadFromDB(): Flow<List<TvShow>> {
                return localDataSource.getAllTvShow().map {
                    DataMapper.mapEntitiesToDomainTvShow(it)
                }
            }

            override fun shouldFetch(data: List<TvShow>?): Boolean = data == null || data.isEmpty()

            override suspend fun createCall(): Flow<ApiResponse<List<TvShowResponse>>> =
                remoteDataSource.fetchTvShow()

            override suspend fun saveCallResult(data: List<TvShowResponse>) {
                val list = DataMapper.responseToEntitiesTvShow(data)
                localDataSource.insertTvShow(list)
            }
        }.asFlow()

    override suspend fun getTvShow(id: Int): Flow<TvShow> =
        remoteDataSource.fetchDetailTvShow(id).map {
            DataMapper.responseToDomainTvShow(it)
        }

    override fun setFavoriteTvShow(tvShow: TvShow, state: Boolean) {
        val entity = DataMapper.domainToEntityTvShow(tvShow)
        appExecutors.diskIO().execute { localDataSource.setFavoriteTvShow(entity, state) }
    }

    override fun getFavoriteTvShow(): LiveData<PagedList<TvShow>> {
        val data = localDataSource.getFavoriteTvShow().map {
            DataMapper.mapEntityToDomainTvShow(it)
        }
        val config = PagedList.Config.Builder()
            .setEnablePlaceholders(false)
            .setInitialLoadSizeHint(4)
            .setPageSize(4)
            .build()

        return LivePagedListBuilder(data, config).build()
    }
}