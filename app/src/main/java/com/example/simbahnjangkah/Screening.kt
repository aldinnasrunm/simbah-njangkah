package com.example.simbahnjangkah

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialogDefaults.shape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.simbahnjangkah.ui.theme.SimbahNjangkahTheme
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

private val scope = CoroutineScope(Dispatchers.IO + CoroutineName("m-scope"))

class Screening : ComponentActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private lateinit var sensor: Sensor
    private var stepCount = 0
    private var lastStep = 0
    private var timer: CountDownTimer? = null
    private var remainingTime by mutableStateOf(0L)
    private var isRunning by mutableStateOf(false)
    val db_test = App.userDatabase.testDataDao()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) == null) {
            Toast.makeText(this, "No step counter sensor available", Toast.LENGTH_SHORT).show()
        } else {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)!!
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST)
        }

        setContent {
            SimbahNjangkahTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
//                    UI

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
                    ){
                        StepCounter()
                    }

                }
            }
        }
    }




    @Composable
    fun BigCircularButton(text: String, onClick: () -> Unit) {
        Surface(
            modifier = Modifier
                .padding(16.dp)
                .width(400.dp)
                .height(400.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.tertiary // Change color as needed

        ) {
            Button(
                onClick = onClick,
                modifier = Modifier
                    .fillMaxSize(),
                shape = CircleShape,
            ) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.displayLarge,
                    color = Color.White // Change text color as needed
                )
            }
        }
    }

        @Preview(showBackground = true)
        @Composable
        fun StepCounter() {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Screening 6 Menit",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(8.dp, 0.dp,0.dp,16.dp  )
                )
                Text(
                    text = "Step count: $stepCount",
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(8.dp)
                )

                BigCircularButton(if (isRunning) "${formatTime(remainingTime)}" else "Mulai") {
                        if (!isRunning)
                            isRunning = true

                }
            }
        }


    private fun formatTime(seconds: Long): String {
        val minutes = seconds / 60
        val remainingSeconds = seconds % 60
        return String.format("%02d:%02d", minutes, remainingSeconds)
    }

    private fun startCountDownTimer() {
        //        timer = object : CountDownTimer(1 * 60 * 1000, 1000) {
//            override fun onTick(millisUntilFinished: Long) {
//                // Update the remaining time
//                remainingTime = millisUntilFinished / 1000
//            }

        timer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                // Update the remaining time
                remainingTime = millisUntilFinished / 1000
            }

            override fun onFinish() {
                SaveData()
                Toast.makeText(this@Screening, "Six minutes finished", Toast.LENGTH_SHORT).show()
            }
        }.start()
    }

    fun SaveData() {
        scope.launch {
            db_test.insertTestData(
                TestData(
                    dateRecorded = GetDateNow(),
                    totalSteps = stepCount,
                    status = true
                )
            )

            if (db_test.getAllTestData().size > 0) {
                Log.e("TAG", "onFinish: Data saved!!")
            } else {
                Log.e("TAG", "onFinish: Data not saved!!")
            }
        }
    }

    fun GetDateNow(): Date {
        return Date()
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (isRunning) {
            var estep = event!!.values[0].toInt()
            if (estep > 0) {
                lastStep = estep - 1
            }
            stepCount += estep - lastStep
            lastStep = estep

            if (stepCount == 1) {
                startCountDownTimer()
            }
        }
        println("Step count: $stepCount")
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.d("OnSensorChanged", "onAccuracyChanged: $accuracy")
    }

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SimbahNjangkahTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = androidx.compose.ui.Alignment.CenterHorizontally
        ){
            StepCounter()
        }
    }
}
}
