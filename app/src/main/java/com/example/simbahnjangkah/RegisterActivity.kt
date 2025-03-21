package com.example.simbahnjangkah

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.PermissionChecker
import androidx.core.text.isDigitsOnly
import com.example.simbahnjangkah.ui.theme.SimbahNjangkahTheme
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

private val db = App.userDatabase.userDao()
private val scope = CoroutineScope(Dispatchers.IO + CoroutineName("m-scope"))

class RegisterActivity : ComponentActivity() {

    val requestPermissionLauncher =
        this.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            permissions.entries.forEach {
                if (it.value) {
                    // Permission is granted, you can proceed with accessing body sensors
                    Toast.makeText(this, "${it.key} permission granted", Toast.LENGTH_SHORT).show()
                } else {
                    // Permission is denied
                    Toast.makeText(this, "${it.key} permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimbahNjangkahTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    ImageBackground(imageResId = R.drawable.bg_textured)
                    RegisterForm()  // call the RegisterForm function
                }
            }
        }

        val permissions = listOf(
            Manifest.permission.POST_NOTIFICATIONS, Manifest.permission.ACTIVITY_RECOGNITION
        )

        val notGrantedPermissions = permissions.filter {
            PermissionChecker.checkSelfPermission(this, it) != PermissionChecker.PERMISSION_GRANTED
        }.toMutableList()

        val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    // Permission is granted, you can proceed with accessing body sensors
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
                    // Request the next permission if there are more to request
                    if (notGrantedPermissions.isNotEmpty()) {
                        requestPermissionLauncher.launch(arrayOf(notGrantedPermissions.removeAt(0)))
                    }
                } else {
                    // Permission is denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }


        if (notGrantedPermissions.isNotEmpty()) {
            requestPermissionLauncher.launch(notGrantedPermissions.removeAt(0))
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

    @Composable
    fun RegisterForm() {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp, 8.dp),

            ) {
            val header = "Registration"
            val name = remember { mutableStateOf("") }
            val age = remember { mutableStateOf("") }
            var selectedGender by remember { mutableStateOf(JenisKelamin.PRIA) }
            val painter: Painter = painterResource(id = R.drawable.logo_hor_gradient)

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
            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = header,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 12.dp, 0.dp, 24.dp),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.background
            )

            Text(
                text = "Name",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(0.dp, 16.dp, 0.dp, 4.dp),
                color = MaterialTheme.colorScheme.onPrimary
            )

            TextField(
                value = name.value,
                onValueChange = { name.value = it },
                label = { Text("Name") },
                modifier = Modifier
                    .padding(0.dp, 0.dp, 0.dp, 4.dp)
                    .fillMaxWidth(),

                )

            Text(
                text = "Age",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(0.dp, 16.dp, 0.dp, 4.dp),
                color = MaterialTheme.colorScheme.onPrimary
            )


            TextField(value = age.value,
                onValueChange = {
                    if (it.isDigitsOnly()) {
                        age.value = it
                    }
                },
                label = { Text("Age") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .padding(0.dp, 0.dp, 0.dp, 4.dp)
                    .fillMaxWidth(),
            )


            Column(
                modifier = Modifier.padding(0.dp, 12.dp, 0.dp, 12.dp)
            ) {
                Text(
                    text = "Gender:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary

                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedGender == JenisKelamin.PRIA,
                        onClick = { selectedGender = JenisKelamin.PRIA },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colorScheme.tertiary,
                            unselectedColor = MaterialTheme.colorScheme.secondary
                        )
                        )
                    Text(
                        text = "Male",
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(start = 8.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedGender == JenisKelamin.WANITA,
                        onClick = { selectedGender = JenisKelamin.WANITA },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = MaterialTheme.colorScheme.tertiary,
                            unselectedColor = MaterialTheme.colorScheme.secondary
                        )

                    )
                    Text(
                        text = "Female",
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.padding(start = 8.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Button(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(0.dp, 12.dp, 0.dp, 42.dp),
                    colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.tertiary),
                    onClick = {

                        if (name.value.isEmpty() || age.value.isEmpty()) {
                            Toast.makeText(this@RegisterActivity, "Please fill the data", Toast.LENGTH_SHORT).show()
                            return@Button
                        }else{
                            val user = User(
                                id = 0,
                                userName = name.value,
                                userAge = age.value.toInt(),
                                userGender = selectedGender.toString(),
                                uid = GenerateUID()
                            )
                            addData(user)
                        }

                    },

                    ) {
                    Text(
                        text = "Register Now",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }

    fun addData(user: User) {
        scope.launch {

            db.upsertUser(user)
            if (db.getAllUsers().size > 0) {
                Intent(this@RegisterActivity, DashboardActivity::class.java).also {
                    startActivity(it)
                    finish()
                }
            }
            scope.cancel()
        }
    }


    private fun GenerateUID(): String {
        //        generate UID with 10 character length
        val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..10).map { _ -> kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get).joinToString("")
    }

    @Composable
    fun Greeting3(name: String, modifier: Modifier = Modifier) {
        Text(
            text = "Hello $name!", modifier = modifier
        )
    }

    enum class JenisKelamin {
        PRIA, WANITA
    }


    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview3() {
        SimbahNjangkahTheme {
            RegisterForm()
        }
    }

}