package com.oogwayapps.branchthreads.api

import com.oogwayapps.branchthreads.models.AuthRequest
import com.oogwayapps.branchthreads.models.AuthResponse
import com.oogwayapps.branchthreads.models.Message
import com.oogwayapps.branchthreads.models.SendMessageRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ThreadsAPI {
    @POST("/api/login")
    suspend fun signInRequest( @Body authRequest: AuthRequest):Response<AuthResponse>

    @GET("/api/messages")
    suspend fun getMessages():Response<List<Message>>

    @POST("/api/messages")
    suspend fun sendMessages(@Body sendMessageRequest: SendMessageRequest):Response<Message>

}