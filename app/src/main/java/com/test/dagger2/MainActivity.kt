package com.test.dagger2

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.test.dagger2.dagger_classes.Car
import com.test.dagger2.dagger_classes.DaggerDaggerComponent
import com.test.dagger2.ui.theme.Dagger2Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class MainActivity : ComponentActivity(), CoroutineScope {
    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    @Inject
    lateinit var car: Car

    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val rem1 = remember { mutableStateOf("Start") }
            val rem2 = remember { mutableStateOf("Start") }
            DaggerDaggerComponent.create().inject(this)
            suspend fun main(){
                val counter = launch{
                    try {
                        for (i in 0..5) {
                            delay(400L)
                            rem1.value = i.toString()
                            Log.d("myLog", rem1.value)
                        }
                    } catch (e: Exception) {
                        cancel()
                    } finally {
                        rem2.value = "End"
                        Log.d("myLog", rem2.value)
                    }
                }
            }
            Column (modifier = Modifier.padding(top = 50.dp, start = 20.dp)){
                Text(rem1.value)
                Text(rem2.value)
                Row {
                    Button(onClick = {
                            launch {
                                main()
                            }
                    }) {
                        Text("Start")
                    }
                    Button(onClick = {
                        rem1.value = "Start"
                        rem2.value = rem1.value
                    }) {
                        Text("Reset")
                    }
                }
            }
        }
    }
}

