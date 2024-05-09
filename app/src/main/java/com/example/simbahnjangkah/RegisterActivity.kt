package com.example.simbahnjangkah

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimbahNjangkahTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    RegisterForm()  // call the RegisterForm function
                }
            }
        }
    }



    @Composable
    fun RegisterForm() {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 8.dp),

            ) {
            val header = "Registrasi\nSimbah Njangkah"
            val name = remember { mutableStateOf("") }
            val age = remember { mutableStateOf("") }
            var selectedGender by remember { mutableStateOf(JenisKelamin.PRIA) }



            Text(
                text = header,
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 12.dp, 0.dp, 24.dp),
                textAlign = TextAlign.Center
            )

            Text(
                text = "Nama",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(0.dp, 16.dp, 0.dp, 4.dp),
            )

            TextField(
                value = name.value,
                onValueChange = { name.value = it },
                label = { Text("Nama") },
                modifier = Modifier
                    .padding(0.dp, 0.dp, 0.dp, 4.dp)
                    .fillMaxWidth(),

                )

            Text(
                text = "Usia",
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.padding(0.dp, 16.dp, 0.dp, 4.dp),
            )


            TextField(
                value = age.value,
                onValueChange = {
                    if (it.isDigitsOnly()) {
                        age.value = it
                    }
                },
                label = { Text("Usia") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .padding(0.dp, 0.dp, 0.dp, 4.dp)
                    .fillMaxWidth()
            )


            Column(
                modifier = Modifier.padding(0.dp, 12.dp, 0.dp, 12.dp)
            ) {
                Text(
                    text = "Pilih Jenis Kelamin:",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedGender == JenisKelamin.PRIA,
                        onClick = { selectedGender = JenisKelamin.PRIA }
                    )
                    Text(
                        text = "Pria",
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    RadioButton(
                        selected = selectedGender == JenisKelamin.WANITA,
                        onClick = { selectedGender = JenisKelamin.WANITA }
                    )
                    Text(
                        text = "Wanita",
                        style = MaterialTheme.typography.labelMedium,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp)
                        .padding(0.dp, 12.dp, 0.dp, 0.dp),
                    onClick = {
                        val user = User(
                            id = 0,
                            userName = name.value,
                            userAge = age.value.toInt(),
                            userGender = selectedGender.toString(),
                            uid = GenerateUID()
                        )
                        addData(user)

                    },

                    ) {
                    Text("Registrasi sekarang")

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


    private fun GenerateUID(): String{
    //        generate UID with 10 character length
        val charPool : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')
        return (1..10)
            .map { _ -> kotlin.random.Random.nextInt(0, charPool.size) }
            .map(charPool::get)
            .joinToString("")
    }

    @Composable
    fun Greeting3(name: String, modifier: Modifier = Modifier) {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
    }

    enum class JenisKelamin {
        PRIA,
        WANITA
    }


    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview3() {
        SimbahNjangkahTheme {
            RegisterForm()
        }
    }

}