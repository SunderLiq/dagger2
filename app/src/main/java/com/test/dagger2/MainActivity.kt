package com.test.dagger2

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.test.dagger2.dagger_classes.Car
import com.test.dagger2.dagger_classes.DaggerDaggerComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
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
            val stoped = remember { mutableStateOf(true) }

            val ShowStopingDialog = remember { mutableStateOf(false) }

            DaggerDaggerComponent.create().inject(this)
                val counter: Job = launch(start = CoroutineStart.LAZY) {
                    stoped.value = false
                    for (i in 0.. 10) {
                        if (stoped.value) {
                            cancel()
                            break
                        }
                        delay(100L)
                        rem1.value = i.toString()
                    }
                    stoped.value = true
                    rem2.value = "End"
                }

            Column(modifier = Modifier.padding(top = 50.dp, start = 20.dp)) {
                Text(rem1.value)
                Text(rem2.value)
                Row (Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween){
                    Button(onClick = {
                        if (stoped.value) counter.start()
                    }) {
                        Text("Start")
                    }
                    Button(onClick = {
                        stoped.value = true
                    }) {
                        Text("Stop")
                    }
                    Button(onClick = {
                        if(stoped.value) {
                            rem1.value = "0"
                            rem2.value = "Start"
                        }
                        else ShowStopingDialog.value = true
                    }) {
                        Text("Reset")
                    }
                }
            }
            if (ShowStopingDialog.value)
                Dialog(onDismissRequest = {ShowStopingDialog.value = false}) {
                    Card(Modifier.fillMaxWidth().height(120.dp).width(8.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(text = "Таймер ещё идёт!", modifier = Modifier
                            .fillMaxSize()
                            .wrapContentSize(Alignment.Center), textAlign = TextAlign.Center)
                    }
                }

        }
    }
}

