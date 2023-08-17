package com.oogwayapps.branchthreads.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.oogwayapps.branchthreads.models.Message
import com.oogwayapps.branchthreads.models.SendMessageRequest
import com.oogwayapps.branchthreads.utils.ResponseResource
import com.oogwayapps.branchthreads.viewmodel.ThreadViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ThreadDetails(navController: NavController,threadViewModel: ThreadViewModel) {

    val threadDetails = threadViewModel.threadDetails
    val responseState by threadViewModel.sendMessageFlow.collectAsState()
    var messageList by remember { mutableStateOf(threadDetails.messages) }
    var newMessage by remember { mutableStateOf(TextFieldValue()) }
    var successUpdateApplied by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        val listState = rememberLazyListState()
        LaunchedEffect(messageList.size) {
            listState.animateScrollToItem(messageList.size)
        }
        LazyColumn(
            state=listState,
            modifier = Modifier.weight(1f),
            contentPadding = PaddingValues(horizontal = 16.dp)
        ) {

            items(messageList.distinct()) { message ->
                MessageItem(message)
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = newMessage,
                onValueChange = { newMessage = it },
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp),
                label = { Text("Type your message") }
            )

            Button(
                onClick = {
                    if (newMessage.text.isNotBlank()) {
                        threadViewModel.sendMessage(SendMessageRequest(
                            newMessage.text,
                            threadDetails.thread_id
                        ))
                        newMessage = TextFieldValue("")
                    }
                }
            ) {
                Text(text = "Send")
            }
        }
    }


    when(responseState){
        is ResponseResource.Success ->{
            if (!successUpdateApplied) {
                (responseState as ResponseResource.Success<Message>).data?.let { response ->
                    messageList = (messageList + response) as MutableList<Message>
                }
                successUpdateApplied = true
            }
        }
        is ResponseResource.Loading ->{
            ProgressBar()
        }
        is ResponseResource.Error ->{
            (responseState as ResponseResource.Error).throwable.message?.let {
                LocalContext.current.showToast(it)
            }
        }
        else -> {

        }
    }
}

@Composable
fun MessageItem(message: Message) {
    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val parsedDate = dateFormatter.parse(message.timestamp)
        val formattedDate = SimpleDateFormat("MMM d, yyyy h:mm a", Locale.getDefault()).format(parsedDate ?: Date())

        Column(modifier = Modifier.weight(1f)) {
            Text(text = message.body, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = formattedDate, color = Color.Gray)
        }

        Spacer(modifier = Modifier.width(16.dp))

        val senderId:String = (message.agent_id ?: message.user_id) as String
        Surface(
            modifier = Modifier.size(40.dp),
            shape = CircleShape,
            color = Color.Gray
        ) {
            Text(
                text = senderId,
                modifier = Modifier
                    .padding(4.dp)
                    .wrapContentHeight(),
                textAlign = TextAlign.Center,
                color = Color.White,
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

    }
}





