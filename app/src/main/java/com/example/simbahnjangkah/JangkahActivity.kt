package com.example.simbahnjangkah

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.simbahnjangkah.ui.theme.SimbahNjangkahTheme
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


private val scope = CoroutineScope(Dispatchers.IO + CoroutineName("m-scope"))

@RequiresApi(Build.VERSION_CODES.O)
class JangkahActivity : ComponentActivity(), SensorEventListener {
    val date = LocalDateTime.now()
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")

    private lateinit var sensorManager: SensorManager
    private lateinit var sensor: Sensor
    private var stepCount by mutableStateOf(0)
    private var lastStep = 0
    private var lastRecordedID = 0
    private var lastRecordedStep by mutableStateOf(0)
    private var isSync = false
    private val target = 5000
    private var stepProxy = 66.0;


    private var isRunning by mutableStateOf(false)
    val db_train = App.userDatabase.trainingDao()
    override fun onCreate(savedInstanceState: Bundle?) {

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) == null) {
            Toast.makeText(this, "No step counter sensor available", Toast.LENGTH_SHORT).show()
        } else {
            sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)!!
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_FASTEST)
        }

        super.onCreate(savedInstanceState)
        setContent {
            SimbahNjangkahTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    StepCounter()
                }
            }
        }

        checkIsRecorded()

    }



    @Preview(showBackground = true)
    @Composable
    fun StepCounter() {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,

        ) {
            Text(
                text = "Daily Step Counter",
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(8.dp, 0.dp,0.dp,16.dp ),
                textAlign = TextAlign.Center
            )

            Text(
                text = "Take at least 5,000 steps every day to achieve optimal results",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(8.dp),
                textAlign = TextAlign.Center
            )

            BigCircularButton(if (isRunning) "$stepCount" else "Start", if (isRunning) true else false) {
                if (!isRunning){
                    isRunning = true
                    Toast.makeText(this@JangkahActivity, "Exercise Started", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    @Composable
    fun BigCircularButton(text: String, visibility: Boolean, onClick: () -> Unit) {
        Surface(
            modifier = Modifier
                .padding(16.dp)
                .width(400.dp)
                .height(400.dp),
            shape = CircleShape,

        ) {
            Button(
                onClick = onClick,
                modifier = Modifier
                    .fillMaxSize(),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.tertiary)
            ) {
                Column {
                    Text(
                        text = text,
                        style = MaterialTheme.typography.displayLarge,
                        color = MaterialTheme.colorScheme.primary // Change text color as needed
                    )
                    if (visibility){
                        Text(
                            text = "Dari 5000 Langkah",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.primary // Change text color as needed

                            )
                    }

                }

            }
        }
    }



    private fun checkIsRecorded(){
        scope.launch {
            var data = db_train.getTrainingByDate(GetDateNow().toString())
            if (data != null) {
                lastRecordedID = data.id
                lastRecordedStep = data.totalSteps
//                return@launch true
            }else{
//                return@launch false
            }
        }
    }

    private fun GetDateNow(): String? {
        val formattedDate = date.format(formatter)
        return formattedDate
    }

    private fun upsertData(step : Int){
        scope.launch {
            db_train.upsertTraining(Training(id = lastRecordedID, dateRecorded = GetDateNow(), totalSteps = step, distance = ((step * stepProxy) / 100.0) ,status = if(step >= target) true else false))
        }
    }

    private fun upsertUpdate(step : Int){
        scope.launch {
            db_train.upsertTraining(Training(dateRecorded = GetDateNow(), totalSteps = step))
        }
    }

    private fun insertData(step : Int){
        scope.launch {
            db_train.insertTraining(Training(dateRecorded = GetDateNow(), totalSteps =  step, distance = ((step * stepProxy) / 100.0), status = false))
            checkIsRecorded()
        }
    }

    private fun insertOrUpdateData(step : Int){
        scope.launch {
            var data = db_train.getTrainingByDate(GetDateNow().toString())
            if (data != null) {
                // Update the existing record
                db_train.updateTraining(dateRecorded = GetDateNow()!!, totalStep = step, status = if(step >= target) true else false)
            } else {
                // Insert a new record
                db_train.insertTraining(Training(dateRecorded = GetDateNow(), totalSteps =  step, distance = ((step * stepProxy) / 100.0), status = false))
            }
            checkIsRecorded()
        }
    }


    override fun onSensorChanged(event: SensorEvent?) {
        if (isRunning) {

            var estep = event!!.values[0].toInt()
            if (estep > 0) {
                lastStep = estep - 1
            }

            if (lastRecordedStep > 0 && !isSync){
                stepCount = lastRecordedStep
                isSync = true
            }

            stepCount += estep - lastStep
            lastStep = estep


//            if (lastRecordedStep == 0 && stepCount > 1){
//                insertData(stepCount)
//                insertOrUpdateData(stepCount)
//            }
            insertOrUpdateData(stepCount)

//                upsertData(stepCount)
//            println("Step count: $stepCount")
            Toast.makeText(this@JangkahActivity, "Steps : $stepCount", Toast.LENGTH_SHORT)
            Log.d("Step Count", "Step: $stepCount ")
        }
    }


    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.d("OnSensorChanged", "onAccuracyChanged: $accuracy")
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview3() {
    SimbahNjangkahTheme {
        Greeting("Android")
    }
}