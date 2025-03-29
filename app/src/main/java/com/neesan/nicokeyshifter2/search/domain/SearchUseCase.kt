package com.neesan.nicokeyshifter2.search.domain

import com.neesan.nicokeyshifter2.search.data.search.SearchRepository
import com.neesan.nicokeyshifter2.search.domain.exception.SearchException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * 動画検索を行うユースケースクラス
 */
class SearchUseCase @Inject constructor(
    private val searchRepository: SearchRepository
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
    ): Flow<List<VideoDomainModel>> = flow {
        // リポジトリを呼び出して検索を実行
        // リポジトリ層で発生した例外は呼び出し元に伝播するので、ここでは特別なエラーハンドリングは不要
        val videos = searchRepository.searchVideos(query, targets, sort, limit)
        emit(videos)
    }
}
