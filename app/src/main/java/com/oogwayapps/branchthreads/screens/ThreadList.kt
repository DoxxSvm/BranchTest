package com.oogwayapps.branchthreads.screens

import android.app.Activity
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.oogwayapps.branchthreads.models.MappedThreadMessages
import com.oogwayapps.branchthreads.models.Message
import com.oogwayapps.branchthreads.utils.ResponseResource
import com.oogwayapps.branchthreads.viewmodel.ThreadViewModel
import java.text.SimpleDateFormat
import java.util.*


@Composable
fun MessageThreadsScreen(mappedThreadMessages: List<MappedThreadMessages>,onThreadClicked: (MappedThreadMessages) -> Unit) {

    LazyColumn {
        items(items = mappedThreadMessages) { thread ->
            MessageThreadItem(thread, onThreadClicked)
            Divider(modifier = Modifier.padding(horizontal = 16.dp), color = Color.Gray)
        }
    }
}

@Composable
fun MessageThreadItem(thread: MappedThreadMessages, onThreadClicked: (MappedThreadMessages) -> Unit) {
    val lastMessage = thread.messages[thread.messages.size - 1]
    Row(
        modifier = Modifier.clickable { onThreadClicked(thread) }
            .padding(16.dp).fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {

        val formattedDate = getFormattedDate(lastMessage)

        Column(modifier = Modifier.weight(1f)) {
            Text(text = lastMessage.body, fontWeight = FontWeight.Bold, fontSize = 16.sp, maxLines = 2)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = formattedDate!!, color = Color.Gray)
        }

        Spacer(modifier = Modifier.width(16.dp))

        val senderId:String = (lastMessage.agent_id ?: lastMessage.user_id) as String
        Surface(
            modifier = Modifier.size(40.dp),
            shape = CircleShape,
            color = Color.Gray
        ) {
            Text(
                text = senderId,
                modifier = Modifier.padding(4.dp).wrapContentHeight(),
                textAlign = TextAlign.Center,
                color = Color.White,
                fontSize = 12.sp
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Icon(imageVector = Icons.Default.ArrowForward, contentDescription = null)
    }

    val context = LocalContext.current as Activity

    BackHandler() {
        context.finish()
    }
}



@Composable
fun ThreadList(threadViewModel: ThreadViewModel,navigate:()->Unit) {
    val responseState by threadViewModel.messageFlow.collectAsState()
    LaunchedEffect(key1 = true ){
        threadViewModel.getMessages()
    }
    when(responseState){
        is ResponseResource.Success ->{
            (responseState as ResponseResource.Success<List<Message>>).data?.let { response->
                val mappedThreadMessages = formatThreadItems(response)
                MessageThreadsScreen(mappedThreadMessages,onThreadClicked = {
                    threadViewModel.threadDetails = it
                    navigate()
                })
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
        else -> {}
    }



}


private fun formatThreadItems(response:List<Message>):List<MappedThreadMessages>{
    val hashmap:HashMap<Int,ArrayList<Message>> = hashMapOf()
    response.forEach{message ->
        if(!hashmap[message.thread_id].isNullOrEmpty()) {
            hashmap[message.thread_id]!!.add(message)
        }
        else hashmap[message.thread_id] = arrayListOf(message)
    }
    val mappedThreadMessages: MutableList<MappedThreadMessages> = arrayListOf()
    hashmap.keys.forEach {
        val sortedThreads = hashmap[it]!!.sortedBy { thread ->
            val parsedDate = getParsedDate(thread)
            parsedDate ?: Date(0)
        } as MutableList<Message>
        mappedThreadMessages.add(MappedThreadMessages(it, sortedThreads))
    }

    mappedThreadMessages.sortByDescending {
        val lastMessage = it.messages[it.messages.size - 1]
        val parsedDate = getParsedDate(lastMessage)
        parsedDate ?: Date(0)
    }
    return mappedThreadMessages

}
private fun getFormattedDate(lastMessage: Message): String? {
    val parsedDate = getParsedDate(lastMessage)
    return SimpleDateFormat("MMM d, yyyy h:mm a", Locale.getDefault()).format(parsedDate ?: Date())
}

private fun getParsedDate(thread: Message): Date? {
    val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
    return dateFormatter.parse(thread.timestamp)
}
