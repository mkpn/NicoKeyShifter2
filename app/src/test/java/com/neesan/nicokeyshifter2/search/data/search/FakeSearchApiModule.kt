package com.neesan.nicokeyshifter2.search.data.search

import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Response
import java.io.IOException
import javax.inject.Singleton

/**
 * テスト用のSearchModule
 * プロダクションのSearchApiの代わりにFakeSearchApiを提供する
 */
@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [SearchApiModule::class] // プロダクションコードにSearchApiを提供するモジュールがあればここに指定
)
object FakeSearchApiModule {
    // テストケースに応じて動作を切り替えるためのフラグ
    var shouldReturnError = false
    var shouldThrowNetworkError = false
    var mockResponseCode = 200

    @Provides
    @Singleton
    fun provideFakeSearchApi(): SearchApi {
        return FakeSearchApi(
            shouldReturnError = shouldReturnError,
            shouldThrowNetworkError = shouldThrowNetworkError,
            mockResponseCode = mockResponseCode
        )
    }
}

/**
 * テスト用のFake SearchApi実装
 */
class FakeSearchApi(
    // テストケースに応じて動作を切り替えるためのフラグ
    private val shouldReturnError: Boolean = false,
    private val shouldThrowNetworkError: Boolean = false,
    private val mockResponseCode: Int = 200,
) : SearchApi {
    // テスト用のモックデータ
    private val mockVideos = listOf(
        Video(
            contentId = "sm12345",
            title = "テスト動画1",
            viewCounter = 1000,
            thumbnailUrl = "https://example.com/thumbnail1.jpg"
        ),
        Video(
            contentId = "sm67890",
            title = "テスト動画2",
            viewCounter = 5000,
            thumbnailUrl = "https://example.com/thumbnail2.jpg"
        ),
        Video(
            contentId = "sm24680",
            title = "キーワード含む動画",
            viewCounter = 3000,
            thumbnailUrl = "https://example.com/thumbnail3.jpg"
        )
    )

    override suspend fun search(
        query: String,
        targets: String,
        fields: String,
        sort: String,
        limit: Int
    ): Response<SearchVideoResponse> {
        // ネットワークエラーの場合
        if (shouldThrowNetworkError) {
            throw IOException("模擬ネットワークエラー")
        }

        // APIエラーの場合
        if (shouldReturnError) {
            return Response.error(
                mockResponseCode,
                "APIエラー".toResponseBody(null)
            )
        }

        return Response.success(SearchVideoResponse(mockVideos))
    }
}