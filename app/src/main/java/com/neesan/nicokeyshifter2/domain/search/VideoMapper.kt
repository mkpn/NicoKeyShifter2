package com.neesan.nicokeyshifter2.domain.search

import com.neesan.nicokeyshifter2.data.search.Video

/**
 * VideoからVideoDomainModelへのマッピングを行うクラス
 */
object VideoMapper {
    /**
     * データレイヤーのVideoをドメインレイヤーのVideoDomainModelに変換する
     *
     * @param video データレイヤーのVideoオブジェクト
     * @return ドメインレイヤーのVideoDomainModelオブジェクト
     */
    fun toVideoDomainModel(video: Video): VideoDomainModel {
        return VideoDomainModel(
            id = video.contentId,
            title = video.title,
            viewCount = video.viewCounter,
            thumbnailUrl = video.thumbnailUrl
        )
    }
} 