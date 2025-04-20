package com.neesan.nicokeyshifter2.search.domain

import com.neesan.nicokeyshifter2.search.data.search.FakeSearchApi
import com.neesan.nicokeyshifter2.search.data.search.FakeSearchApiModule
import com.neesan.nicokeyshifter2.search.data.search.SearchApi
import com.neesan.nicokeyshifter2.search.domain.exception.SearchException
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject

@Suppress("NonAsciiCharacters")
@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(application = HiltTestApplication::class)
class SearchUseCaseTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @Inject
    lateinit var searchVideoUseCase: SearchVideoUseCase

    @Inject
    lateinit var fakeSearchApi: SearchApi

    @Test
    fun `正常系 - 検索結果が取得できること`() = runTest {
        // 準備
        FakeSearchApiModule.apply {
            shouldReturnError = false
            shouldThrowNetworkError = false
            mockResponseCode = 200
        }
        hiltRule.inject()

        // テスト実行
        val result = searchVideoUseCase.invoke("テスト").first()

        // 検証
        assertEquals(2, result.size)

        val firstVideo = result[0]
        assertEquals("sm67890", firstVideo.id)
        assertEquals("テスト動画2", firstVideo.title)
        assertEquals(5000, firstVideo.viewCount)
        assertEquals("https://example.com/thumbnail2.jpg", firstVideo.thumbnailUrl)

        val secondVideo = result[1]
        assertEquals("sm12345", secondVideo.id)
        assertEquals("テスト動画1", secondVideo.title)
        assertEquals(1000, secondVideo.viewCount)
        assertEquals("https://example.com/thumbnail1.jpg", secondVideo.thumbnailUrl)
    }

    @Test
    fun `正常系 - 検索結果が空の場合は空リストが返ること`() = runTest {
        // 準備
        FakeSearchApiModule.apply {
            shouldReturnError = false
            shouldThrowNetworkError = false
            mockResponseCode = 200
        }
        hiltRule.inject()

        // テスト実行
        val result = searchVideoUseCase.invoke("存在しないキーワード").first()

        // 検証
        assertTrue(result.isEmpty())
    }

    @Test
    fun `正常系 - キーワードでフィルタリングされること`() = runTest {
        // 準備
        FakeSearchApiModule.apply {
            shouldReturnError = false
            shouldThrowNetworkError = false
            mockResponseCode = 200
        }
        hiltRule.inject()

        // テスト実行
        val result = searchVideoUseCase.invoke("キーワード").first()

        // 検証
        assertEquals(1, result.size)
        assertEquals("sm24680", result[0].id)
        assertEquals("キーワード含む動画", result[0].title)
    }

    @Test
    fun `異常系 - APIエラーの場合はApiErrorがスローされること`() {
        // 準備
        FakeSearchApiModule.apply {
            shouldReturnError = true
            mockResponseCode = 404
        }
        hiltRule.inject()

        // テスト実行
        assertThrows(SearchException.ApiError::class.java) {
            runTest {
                searchVideoUseCase.invoke("テスト").collect()
            }
        }
    }

    @Test
    fun `正常系 - ソート順が反映されること`() = runTest {
        // 準備
        FakeSearchApiModule.apply {
            shouldReturnError = false
            shouldThrowNetworkError = false
            mockResponseCode = 200
        }
        hiltRule.inject()

        // テスト実行 - 再生数昇順
        val resultAsc = searchVideoUseCase.invoke("テスト", sort = "viewCounter").first()

        // 検証 - 昇順の場合
        assertEquals(2, resultAsc.size)
        assertEquals(1000, resultAsc[0].viewCount) // 少ない方が先
        assertEquals(5000, resultAsc[1].viewCount)

        // テスト実行 - 再生数降順（デフォルト）
        val resultDesc = searchVideoUseCase.invoke("テスト").first()

        // 検証 - 降順の場合
        assertEquals(2, resultDesc.size)
        assertEquals(5000, resultDesc[0].viewCount) // 多い方が先
        assertEquals(1000, resultDesc[1].viewCount)
    }

    @Test
    fun `正常系 - limit値が反映されること`() = runTest {
        // 準備
        FakeSearchApiModule.apply {
            shouldReturnError = false
            shouldThrowNetworkError = false
            mockResponseCode = 200
        }
        hiltRule.inject()

        // テスト実行 - limit = 1
        val result = searchVideoUseCase.invoke("テスト", limit = 1).first()

        // 検証
        assertEquals(1, result.size) // 2件あるうちの1件だけ取得される
    }
} 