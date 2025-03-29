package com.neesan.nicokeyshifter2.search.domain

/**
 * 動画のドメインモデル
 */
data class VideoDomainModel(
    val id: String,
    val title: String,
    val viewCount: Int,
    val thumbnailUrl: String
) 