package com.develop.data.paging.base

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.develop.common.result.Result
import com.develop.local.dao.BaseDao
import com.develop.local.dao.RemoteKeyDao
import com.develop.local.entities.RemoteKeyEntity
import com.develop.network.until.GenericNetworkResponse

@OptIn(ExperimentalPagingApi::class)
abstract class BasePagingMediator<Entity:Any, ApiModel:Any>(
    private val dao: BaseDao<Entity>,
    private val remoteKeysDao: RemoteKeyDao,
    private val remoteKeyType:Int,
): RemoteMediator<Int, Entity>() {

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, Entity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: 1
            }
            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                remoteKeys?.prevKey ?: return MediatorResult.Success(endOfPaginationReached = true)
            }
            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                remoteKeys?.nextKey ?: 1
            }
        }

        return when(val res = apiCall(page)){
            is Result.Error -> MediatorResult.Error(Throwable())
            is Result.Success -> saveData(res.data, loadType, page)
        }
    }

    abstract suspend fun apiCall(page: Int):GenericNetworkResponse<List<ApiModel>>

    abstract fun toRemoteKey(prevKey: Int?, nextKey:Int?, item:ApiModel): RemoteKeyEntity

    abstract fun toEntity(item:ApiModel):Entity

    abstract suspend fun findRemoteKey(item:Entity?):RemoteKeyEntity?

    private suspend fun saveData(items:List<ApiModel>, loadType: LoadType, page: Int): MediatorResult {
        val endOfPaging = items.isEmpty()

        if (loadType == LoadType.REFRESH){
            remoteKeysDao.deleteAllByType(remoteKeyType)
            dao.deleteAllItems()
        }
        val prevKey = if (page == 1) null else page - 1
        val nextKey = if (endOfPaging) null else page + 1
        val key = items.map {
            toRemoteKey(prevKey, nextKey, it)
        }
        remoteKeysDao.insertAllItems(key)
        val entities = items.mapIndexed { index, item ->
            toEntity(item)
        }
        dao.insert(entities)

        return MediatorResult.Success(endOfPaginationReached = endOfPaging)
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, Entity>): RemoteKeyEntity? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()
            ?.let { item ->
                findRemoteKey(item)
            }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, Entity>): RemoteKeyEntity? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()
            ?.let { item ->
                findRemoteKey(item)
            }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, Entity>): RemoteKeyEntity? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.let { item ->
                findRemoteKey(item)
            }
        }
    }

}