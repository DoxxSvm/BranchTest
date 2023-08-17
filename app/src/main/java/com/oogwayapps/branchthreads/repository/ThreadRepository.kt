package com.oogwayapps.branchthreads.repository

import com.oogwayapps.branchthreads.api.ThreadsAPI
import com.oogwayapps.branchthreads.models.AuthRequest
import com.oogwayapps.branchthreads.models.AuthResponse
import com.oogwayapps.branchthreads.models.Message
import com.oogwayapps.branchthreads.models.SendMessageRequest
import com.oogwayapps.branchthreads.utils.ResponseResource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ThreadRepository @Inject constructor(private val threadsAPI: ThreadsAPI): BaseRepository() {

    suspend fun signInReq(authRequest: AuthRequest):Flow<ResponseResource<AuthResponse>>{
        return safeApiCall {
            threadsAPI.signInRequest(authRequest)
        }
    }

    suspend fun getMessages():Flow<ResponseResource<List<Message>>>{
        return safeApiCall {
            threadsAPI.getMessages()
        }
    }

    suspend fun sendMessages(sendMessageRequest: SendMessageRequest):Flow<ResponseResource<Message>>{
        return safeApiCall {
            threadsAPI.sendMessages(sendMessageRequest)
        }
    }
}