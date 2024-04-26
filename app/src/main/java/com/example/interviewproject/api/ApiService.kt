package com.example.interviewproject.api

import com.example.interviewproject.api.response.Post
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("posts")
    suspend fun getPosts(
        @Query("_page") page: Int,
        @Query("_limit") limit: Int
    ): Response<ArrayList<Post>>

}