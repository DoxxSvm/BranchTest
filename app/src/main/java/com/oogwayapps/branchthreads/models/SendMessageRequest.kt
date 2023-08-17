package com.oogwayapps.branchthreads.models

data class SendMessageRequest(
    val body: String,
    val thread_id: Int
)