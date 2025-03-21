package com.example.simbahnjangkah

import android.Manifest
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.PermissionChecker
import com.example.simbahnjangkah.ui.theme.SimbahNjangkahTheme
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    private val scope = CoroutineScope(Dispatchers.IO + CoroutineName("m-scope"))

    val db = App.userDatabase.userDao()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimbahNjangkahTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.primary
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
//                        Greeting("Simbah Njangkah")
                        VectorImage(vectorResId = R.drawable.logo_spachscreen)
                    }
                }
            }
        }

        launchActivity()

    }

    private fun launchActivity() {
        scope.launch {
            var data = fetchData()
            delay(2000)
            if (data > 0) {
                Intent(this@MainActivity, DashboardActivity::class.java).also {
                    startActivity(it)
                    finish()
                }
            } else {
                Intent(this@MainActivity, WelcomingActivity::class.java).also {
                    startActivity(it)
                    finish()
                }
            }
        }
    }

    private fun fetchData(): Int {
        val total = db.getAllUsers().size
        return total
    }


    @Composable
    fun Greeting(name: String, modifier: Modifier = Modifier) {
        Text(
            text = "$name",
            modifier = modifier
        )

    }

    @Composable
    fun VectorImage(vectorResId: Int) {
        val painter: Painter = painterResource(id = vectorResId)
        Image(
            painter = painter,
            contentDescription = null
        )
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        SimbahNjangkahTheme {
            Greeting("Happy Walking")
        }
    }
}