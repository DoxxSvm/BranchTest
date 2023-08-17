package com.oogwayapps.branchthreads.viewmodel

import android.telecom.Call
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oogwayapps.branchthreads.models.*
import com.oogwayapps.branchthreads.repository.ThreadRepository
import com.oogwayapps.branchthreads.utils.ResponseResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThreadViewModel @Inject constructor(private val repository: ThreadRepository):ViewModel(){


    private val _loginFlow: MutableStateFlow<ResponseResource<AuthResponse>> = MutableStateFlow(ResponseResource.Empty())
    val loginFlow: StateFlow<ResponseResource<AuthResponse>> = _loginFlow.asStateFlow()

    private val _messagesFlow: MutableStateFlow<ResponseResource<List<Message>>> = MutableStateFlow(ResponseResource.Empty())
    val messageFlow: StateFlow<ResponseResource<List<Message>>> = _messagesFlow.asStateFlow()

    private val _sendMessageFlow: MutableStateFlow<ResponseResource<Message>> = MutableStateFlow(ResponseResource.Empty())
    val sendMessageFlow: StateFlow<ResponseResource<Message>> = _sendMessageFlow.asStateFlow()

    lateinit var threadDetails: MappedThreadMessages


    fun signInReq(authRequest: AuthRequest) {
        viewModelScope.launch {
            repository.signInReq(authRequest).collect(){
                _loginFlow.value = it
            }
        }
    }

    fun getMessages(){
        viewModelScope.launch {
            repository.getMessages().collect(){
                _messagesFlow.value = it
            }
        }
    }

    fun sendMessage(sendMessageRequest: SendMessageRequest){
        viewModelScope.launch {
            repository.sendMessages(sendMessageRequest).collect(){
                _sendMessageFlow.value = it
            }
        }
    }

}