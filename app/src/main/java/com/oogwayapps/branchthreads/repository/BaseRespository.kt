package com.oogwayapps.branchthreads.repository

import android.util.Log
import com.oogwayapps.branchthreads.utils.ResponseResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.json.JSONObject
import retrofit2.Response

abstract class BaseRepository {

    suspend fun <T> safeApiCall(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        apiCall:suspend ()-> Response<T>
    ): Flow<ResponseResource<T>> = flow {

        emit(ResponseResource.Loading<T>())
        val response = apiCall()
        print(response.message().toString())
        if (response.isSuccessful) {
            emit(ResponseResource.Success(response.body()))
            return@flow
        }
        val jsonObj = JSONObject(response.errorBody()!!.charStream().readText())
        val throwable = Throwable(message = jsonObj.getString("error"))
        emit(ResponseResource.Error(throwable = throwable))

    }.catch { e->
        e.printStackTrace()
        emit(ResponseResource.Error(throwable = e.fillInStackTrace()))
    }.flowOn(dispatcher)



}
