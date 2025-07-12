package com.neesan.domain.favorite

import com.neesan.data.favorite.FavoriteRepository
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever

@Suppress("NonAsciiCharacters", "TestFunctionName")
class CheckIsFavoriteUseCaseTest {

    private lateinit var favoriteRepository: FavoriteRepository
    private lateinit var checkIsFavoriteUseCase: CheckIsFavoriteUseCase

    @Before
    fun setup() {
        favoriteRepository = mock()
        checkIsFavoriteUseCase = CheckIsFavoriteUseCase(favoriteRepository)
    }

    @Test
    fun お気に入りに登録済みの動画の場合trueが返ること() = runTest {
        // モックの振る舞いを設定
        whenever(favoriteRepository.isFavorite("sm12345"))
            .thenReturn(true)

        // テスト実行
        val result = checkIsFavoriteUseCase.invoke("sm12345")

        // 検証
        assertTrue(result)
    }

    @Test
    fun お気に入りに未登録の動画の場合falseが返ること() = runTest {
        // モックの振る舞いを設定
        whenever(favoriteRepository.isFavorite("sm99999"))
            .thenReturn(false)

        // テスト実行
        val result = checkIsFavoriteUseCase.invoke("sm99999")

        // 検証
        assertFalse(result)
    }
}