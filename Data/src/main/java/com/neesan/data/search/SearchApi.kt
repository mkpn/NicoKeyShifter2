package com.neesan.data.search

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchApi {
    @GET("video/contents/search")
    suspend fun search(
        @Query("q") query: String,
        @Query("targets") targets: String = "title",
        @Query("fields") fields: String = "contentId,title,viewCounter,thumbnailUrl",
        @Query("_sort") sort: String = "-viewCounter",
        @Query("_limit") limit: Int = 100,
    ): Response<SearchVideoResponse>
}