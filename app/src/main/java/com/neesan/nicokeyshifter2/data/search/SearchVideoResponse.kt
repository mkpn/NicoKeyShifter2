package com.neesan.nicokeyshifter2.data.search

import kotlinx.serialization.Serializable

@Serializable
data class SearchVideoResponse(val data: List<Video>)