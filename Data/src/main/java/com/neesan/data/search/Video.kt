package com.neesan.data.search

import kotlinx.serialization.Serializable

@Serializable
data class Video(
    val contentId: String,
    val title: String,
    val viewCounter: Int,
    val thumbnailUrl: String
)