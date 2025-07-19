package com.neesan.domain.search

import com.neesan.data.favorite.FavoriteRepository
import com.neesan.data.search.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.first
import javax.inject.Inject

/**
 * 動画検索を行うユースケースクラス
 */
class SearchVideoUseCase @Inject constructor(
    private val searchRepository: SearchRepository,
    private val favoriteRepository: FavoriteRepository
) {
    /**
     * キーワードで動画を検索する
     *
     * @param query 検索キーワード
     * @param targets 検索対象（デフォルトはタイトル）
     * @param sort ソート方法（デフォルトは再生数降順）
     * @param limit 取得件数（デフォルトは100件）
     * @return 検索結果のFlow
     * @throws SearchException 検索に失敗した場合
     */
    fun invoke(
        query: String,
        targets: String = "title",
        sort: String = "-viewCounter",
        limit: Int = 100
    ): Flow<List<VideoDomainModel>> {
        return flow {
            searchRepository.searchVideos(query, targets, sort, limit).collect {
                // 取得した動画リストをVideoMapperを使って変換
                val videos = it.data.map { video ->
                    val videoDomainModel = VideoMapper.toVideoDomainModel(video)
                    // お気に入り状態を確認
                    val isFavorite = favoriteRepository.isFavorite(videoDomainModel.id).first()
                    videoDomainModel.copy(isFavorite = isFavorite)
                }
                emit(videos)
            }
        }
    }
}
