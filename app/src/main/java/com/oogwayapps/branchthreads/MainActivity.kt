package com.oogwayapps.branchthreads

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.oogwayapps.branchthreads.screens.LoginScreen
import com.oogwayapps.branchthreads.screens.ThreadDetails
import com.oogwayapps.branchthreads.screens.ThreadList
import com.oogwayapps.branchthreads.viewmodel.ThreadViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
    }
}

@Composable
fun App(){
    val navController = rememberNavController()
    val threadViewModel : ThreadViewModel = viewModel()
    NavHost(navController = navController, startDestination = "ThreadList"){
        composable(route = "Login"){
            LoginScreen(threadViewModel){
                navController.navigate("ThreadList")
            }
        }
        composable(route = "ThreadList"){
            ThreadList(threadViewModel){
                navController.navigate("ThreadDetails")
            }
        }

        composable(route = "ThreadDetails"){
            ThreadDetails(navController,threadViewModel)
        }
    }
}
