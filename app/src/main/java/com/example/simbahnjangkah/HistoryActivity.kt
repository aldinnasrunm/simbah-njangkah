package com.example.simbahnjangkah

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.simbahnjangkah.ui.theme.SimbahNjangkahTheme
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private val db_training = App.userDatabase.trainingDao()
private var trainingData by mutableStateOf(mutableListOf<List<String>>())
private val scope = CoroutineScope(Dispatchers.IO + CoroutineName("m-scope"))


class HistoryActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimbahNjangkahTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    TbData(data = trainingData)
                }
            }
        }

        scope.launch {
            updateTrainingData()
        }
    }

    //    @Composable
//    fun TbData(data: List<List<String>>) {
//        var header = listOf("No", "Tanggal", "Jumlah Langkah", "Jarak")
//        Column {
//            LazyColumn(modifier = Modifier.fillMaxWidth()) {
//                itemsIndexed(header) { rowIndex, rowData ->
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.SpaceEvenly
//                    ) {
//                        header.forEachIndexed { colIndex, item ->
//                            TableCell(text = item, isFirstColumn = colIndex == 0)
//                        }
//                    }
//                }
//            }
//            LazyColumn(modifier = Modifier.fillMaxWidth()) {
//                itemsIndexed(data) { rowIndex, rowData ->
//                    Row(
//                        modifier = Modifier.fillMaxWidth(),
//                        verticalAlignment = Alignment.CenterVertically,
//                        horizontalArrangement = Arrangement.SpaceEvenly
//                    ) {
//                        rowData.forEachIndexed { colIndex, item ->
//                            TableCell(text = item, isFirstColumn = colIndex == 0)
//                        }
//                    }
//                }
//            }
//        }
//    }
    @Composable
    fun TbData(data: List<List<String>>) {
        var header = listOf("No", "Tanggal", "Jumlah Langkah", "Jarak")
        Column {
            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        header.forEachIndexed { _, item ->
                            TableCell(
                                text = item,
                                isFirstColumn = false
                            ) // Header row shouldn't have isFirstColumn set to true
                        }
                    }
                }
                itemsIndexed(data) { rowIndex, rowData ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        rowData.forEachIndexed { colIndex, item ->
                            TableCell(text = item, isFirstColumn = colIndex == 0)
                        }
                    }
                }
            }
        }
    }


    @Composable
    fun TableCell(text: String, isFirstColumn: Boolean) {
        Surface(
            modifier = Modifier
                .padding(4.dp),
            color = if (isFirstColumn) Color.LightGray else Color.White
        ) {
            Text(
                text = text,
                modifier = Modifier.padding(8.dp),
                color = Color.Black
            )
        }
    }




    suspend fun updateTrainingData() {
        if (db_training.getAllTrainings().isNotEmpty()) {
            val data = db_training.getAllTrainings()
            trainingData = data.map {
                listOf(
                    it.id.toString(),
                    it.dateRecorded.toString(),
                    it.totalSteps.toString(),
                    it.distance.toString(),
                )
            }.toMutableList()
        } else {
            trainingData = mutableListOf(listOf("No data", "No data", "No data", "No data"))
        }
//        val data = db_training.getAllTrainings()
//        trainingData = data.map {
//            listOf(
//                it.id.toString(),
//                it.dateRecorded.toString(),
//                it.totalSteps.toString(),
//                it.status.toString()
//            )
//        }.toMutableList()


//        Log.d("TAG", "onCreate: $data")
    }

    @Composable
    fun Greeting3(name: String, modifier: Modifier = Modifier) {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        SimbahNjangkahTheme {
            Greeting3("Android")
        }
    }
}