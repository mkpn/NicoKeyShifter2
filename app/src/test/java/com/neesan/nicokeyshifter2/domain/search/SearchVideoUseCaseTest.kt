package com.neesan.nicokeyshifter2.domain.search

import com.neesan.core.exception.SearchException
import com.neesan.data.search.SearchApi
import com.neesan.nicokeyshifter2.data.search.FakeSearchApiModule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
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
class SearchVideoUseCaseTest {

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
        assertEquals(3, result.size)
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
} 