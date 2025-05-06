package com.neesan.data.search

import kotlinx.serialization.Serializable

@Serializable
data class SearchVideoResponse(val data: List<Video>)