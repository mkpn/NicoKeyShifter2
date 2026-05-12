package com.neesan.domain.search

import com.neesan.data.favorite.FavoriteRepository
import com.neesan.data.search.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
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
     * お気に入りDBのFlowと結合しているため、お気に入り一覧側で追加/削除されると
     * 検索結果のisFavoriteも自動的に最新化される。
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
        return searchRepository.searchVideos(query, targets, sort, limit)
            .combine(favoriteRepository.getAllFavoriteVideos()) { response, favorites ->
                val favoriteIds = favorites.mapTo(mutableSetOf()) { it.videoId }
                response.data.map { video ->
                    VideoMapper.toVideoDomainModel(video)
                        .copy(isFavorite = favoriteIds.contains(video.contentId))
                }
            }
    }
}
