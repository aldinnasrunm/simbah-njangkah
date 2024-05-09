package com.example.simbahnjangkah

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.simbahnjangkah.ui.theme.SimbahNjangkahTheme
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class DashboardActivity : ComponentActivity() {
    private val db_user  = App.userDatabase.userDao()
    private val db_test  = App.userDatabase.testDataDao()
    private val db_train  = App.userDatabase.trainingDao()
    private val scope = CoroutineScope(Dispatchers.IO + CoroutineName("m-scope"))

    private var isTested by mutableStateOf(0)
    private var isDismissed by mutableStateOf(false)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimbahNjangkahTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),

                    color = MaterialTheme.colorScheme.background
                ) {
                    UserInfo()
                    LaunchedEffect(key1 = isTested) {
                        chekTested()
                    }
                    if (isTested == 0 && !isDismissed) {
                        DialogCheck()
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun UserInfo() {
        var name by remember {
            mutableStateOf("Nama User")
        }
        var age by remember {
            mutableStateOf(0)
        }
        var gender by remember {
            mutableStateOf("Gender User")
        }

        LaunchedEffect(true) {
            scope.launch {
                name = db_user.getUserName()
                age = db_user.getUserAge()
                gender = db_user.getUserGender()
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(text = name, style = MaterialTheme.typography.bodySmall)
                Text(text = age.toString(), style = MaterialTheme.typography.bodySmall)
                Text(text = gender, style = MaterialTheme.typography.bodySmall)


            }
            Button(onClick = {
                deleteUser()
            }) {
                Text(text = "Delete User")
            }

            Button(onClick = {
                Intent(this@DashboardActivity, JangkahActivity::class.java).also {
                    startActivity(it)
                }
            }) {
                Text(text = "Mulai Latihan 5000 Jangkah")
            }

            Button(onClick = {
                deleteLatihanData()
            }) {
                Text(text = "Delete Latihan Data")
            }
        }
    }

    private fun deleteLatihanData() {
        scope.launch {
            db_train.deleteAllTrainings()
        }
    }

    override fun onPostResume() {
        super.onPostResume()
        chekTested()

    }

    @Composable
    private fun DialogCheck() {
        val openDialog = remember { mutableStateOf(true) }

        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            title = { Text("Lakukan Screening") },
            text = { Text("Ayo lakukan screening penggunaan aplikasi selama 6 menit") },
            confirmButton = {
                Button(
                    onClick = {
                        startActivity(Intent(this@DashboardActivity, Screening::class.java))
//                        finish()
                    }
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        // Dismiss the dialog
                        openDialog.value = false
//                        isDismissed = true

                    }
                ) {
                    Text("Dismiss")
                }
            }
        )

    }

    private fun chekTested(){
        scope.launch {
            if (db_test.getAllTestData().size > 0 ){
                isTested = 1
            }
        }
    }

    private fun deleteUser() {
        scope.launch {
            db_user.deleteAllUsers()
            if (db_user.getAllUsers().isEmpty()) {
                db_test.deleteAllTestDatas()
                startActivity(Intent(this@DashboardActivity, RegisterActivity::class.java))
                finish()
                scope.cancel()
            }
        }
    }
}


@Composable
fun Greeting2(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    SimbahNjangkahTheme {
        Greeting2("Android")
    }
}