package com.neesan.nicokeyshifter2.search.data.search

import com.neesan.nicokeyshifter2.search.domain.exception.SearchException
import com.neesan.nicokeyshifter2.search.domain.VideoMapper
import com.neesan.nicokeyshifter2.search.domain.VideoDomainModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

/**
 * 動画データへのアクセスを提供するリポジトリ
 */
@Singleton
class SearchRepository @Inject constructor(
    private val searchApi: SearchApi,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO
) {

    /**
     * キーワードで動画を検索する
     *
     * @param query 検索キーワード
     * @param targets 検索対象
     * @param sort ソート方法
     * @param limit 取得件数
     * @return 検索結果の動画リスト
     * @throws SearchException.ApiError APIエラーが発生した場合
     * @throws SearchException.UnexpectedError その他の予期せぬエラーが発生した場合
     */
    fun searchVideos(
        query: String,
        targets: String,
        sort: String,
        limit: Int
    ): Flow<List<VideoDomainModel>> {
        return flow {
            val response = searchApi.search(
                query = query,
                targets = targets,
                sort = sort,
                limit = limit
            )

            if (response.isSuccessful) {
                val videos = response.body()?.data ?: emptyList()
                emit(videos.map { VideoMapper.toVideoDomainModel(it) })
            } else {
                throw SearchException.ApiError(response.code())
            }
        }.flowOn(coroutineDispatcher)
    }
} 