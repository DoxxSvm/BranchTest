package com.oogwayapps.branchthreads.models

data class Message(
    val agent_id: String?,
    val body: String,
    val id: Int,
    val thread_id: Int,
    val timestamp: String,
    val user_id: String
)

data class MappedThreadMessages(
    val thread_id: Int,
    val messages: MutableList<Message>
)