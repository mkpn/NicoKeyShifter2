package com.neesan.nicokeyshifter2.search.domain.exception

/**
 * 検索関連の例外の基底クラス
 */
sealed class SearchException(message: String, cause: Throwable? = null) : Exception(message, cause) {
    
    /**
     * APIからのレスポンスが失敗した場合の例外
     *
     * @param statusCode HTTPステータスコード
     */
    class ApiError(val statusCode: Int) : SearchException("検索APIエラー: ステータスコード $statusCode")
    
    /**
     * ネットワーク接続エラーの例外
     */
    class NetworkError(cause: Throwable) : SearchException("ネットワーク接続エラー", cause)
    
    /**
     * 予期せぬエラーの例外
     */
    class UnexpectedError(cause: Throwable) : SearchException("予期せぬエラーが発生しました", cause)
} 