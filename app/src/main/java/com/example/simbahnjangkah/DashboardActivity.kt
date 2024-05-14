package com.example.simbahnjangkah

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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
                    color = MaterialTheme.colorScheme.onPrimary
                ) {
                    ImageBackground(imageResId = R.drawable.bg_dashboard)
                    Column {
                        val painter: Painter = painterResource(id = R.drawable.logo_white)
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(0.dp, 80.dp, 0.dp, 0.dp)
                        ) {
                            Image(
                                painter = painter,
                                contentDescription = null,
                            )
                        }
                        UserInfo()
                        mainScreen()
                    }

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

    @Composable
    fun ImageBackground(imageResId: Int) {
        val painter: Painter = painterResource(id = imageResId)

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Image(
                painter = painter,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
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
                .padding(16.dp, 24.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                val painterProfile = painterResource(id = R.drawable.ic_profile)

                Image(
                    painter = painterProfile,
                    contentDescription = null,
                    modifier = Modifier.padding(0.dp, 0.dp, 12.dp, 0.dp)
                    )
                Column {

                    Text(text = "Halo,", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                    Text(text = if(gender == "PRIA") "Mbah $name" else "Mbah, $name",  color = MaterialTheme.colorScheme.primary)
                }

            }
//            Button(onClick = {
//                deleteUser()
//            }) {
//                Text(text = "Delete User")
//            }

//            Button(onClick = {
//                Intent(this@DashboardActivity, JangkahActivity::class.java).also {
//                    startActivity(it)
//                }
//            }) {
//                Text(text = "Mulai Latihan 5000 Jangkah")
//            }

//            Button(onClick = {
//                deleteLatihanData()
//            }) {
//                Text(text = "Delete Latihan Data")
//            }
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
    private fun mainScreen() {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
//            create box with rounded corners

//            top button
            Surface(
                modifier = Modifier
                    .padding(16.dp, 8.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(32.dp),
                color = MaterialTheme.colorScheme.tertiary,
                onClick = {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        Intent(this@DashboardActivity, JangkahActivity::class.java).also {
                            startActivity(it)
                        }
                    } else{
                        Toast.makeText(this@DashboardActivity, "Versi android belum mendukung, perbarui perangkat anda", Toast.LENGTH_SHORT).show()
                    }
                }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp)
                ) {
                    val ic_run = painterResource(id = R.drawable.ic_jangkah)
                    Image(
                        painter = ic_run,
                        contentDescription = null,
                        modifier = Modifier
                            .padding(0.dp, 0.dp, 0.dp, 16.dp)
                    )
                    Text(
                        text = "Latihan 5000 Jangkah",
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Text(text = "Ayo latihan 5000 langkah setiap hari untuk menjaga kebugaran dan mengurangi risiko jatuh bagi para lansia",
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color(0xFF76522F)
                    )

                }
            }

            Row {
//                Button 2
                Surface(
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f),
                    shape = RoundedCornerShape(32.dp),
                    color = MaterialTheme.colorScheme.onPrimary,
                    border = BorderStroke(2.dp, MaterialTheme.colorScheme.secondary),
                    onClick = {
                        startActivity(Intent(this@DashboardActivity, Screening::class.java))
                    }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(22.dp)
                    ) {
                        val ic_run = painterResource(id = R.drawable.ic_progress)
                        Image(
                            painter = ic_run,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(0.dp, 0.dp, 0.dp, 16.dp)
                        )
                        Text(
                            text = "Screening Ulang",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(text = "lakukan screening ulang untuk membandingkan hasil latihan",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.primary
                        )

                    }
                }
//                Button 3
                Surface(
                    modifier = Modifier
                        .padding(8.dp)
                        .weight(1f),
                    shape = RoundedCornerShape(32.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    onClick = {
                        startActivity(Intent(this@DashboardActivity, HistoryActivity::class.java))
                    }
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(22.dp)
                    ) {
                        val ic_run = painterResource(id = R.drawable.ic_calendar)
                        Image(
                            painter = ic_run,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(0.dp, 0.dp, 0.dp, 16.dp)
                        )
                        Text(
                            text = "Lihat Histori Latihan",
                            style = MaterialTheme.typography.headlineSmall,
                            color = MaterialTheme.colorScheme.background
                        )
                        Text(text = "LIhat histori latihan yang sudah dilakukan sebelumnya",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.background
                        )

                    }
                }
            }



        }
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
                    Text("Lakukan Screening")
                }
            },
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