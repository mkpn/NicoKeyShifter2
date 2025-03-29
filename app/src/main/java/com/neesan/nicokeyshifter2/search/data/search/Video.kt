package com.neesan.nicokeyshifter2.search.data.search

import kotlinx.serialization.Serializable

@Serializable
data class Video(
    val contentId: String,
    val title: String,
    val viewCounter: Int,
    val thumbnailUrl: String
)