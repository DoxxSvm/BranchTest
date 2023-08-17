package com.oogwayapps.branchthreads.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.oogwayapps.branchthreads.models.AuthRequest
import com.oogwayapps.branchthreads.models.AuthResponse
import com.oogwayapps.branchthreads.utils.ResponseResource
import com.oogwayapps.branchthreads.utils.TokenManager
import com.oogwayapps.branchthreads.viewmodel.ThreadViewModel

@Composable
fun LoginScreen(threadViewModel: ThreadViewModel,navigate:()->Unit) {

    val responseState by threadViewModel.loginFlow.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        ReusableOutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = "Email"
        )

        ReusableOutlinedTextField(value = password,
            onValueChange = { password = it },
            label = "Password"
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                threadViewModel.signInReq(AuthRequest(email,password))
                errorMessage = false
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Login")
        }
    }

    when(responseState){
        is ResponseResource.Success ->{
            (responseState as ResponseResource.Success<AuthResponse>).data?.let { response->
                TokenManager(LocalContext.current).storeAuthToken(response.auth_token)
                navigate()
            }
        }
        is ResponseResource.Loading ->{
            ProgressBar()
        }
        is ResponseResource.Error ->{
            if(!errorMessage){
                (responseState as ResponseResource.Error).throwable.message?.let {
                    LocalContext.current.showToast(it)
                }
                errorMessage = true
            }

        }
        else -> {}
    }

}

@Composable
fun ReusableOutlinedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        singleLine = true,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    )
}

fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

@Composable
fun ProgressBar(){
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        CircularProgressIndicator()
    }
}