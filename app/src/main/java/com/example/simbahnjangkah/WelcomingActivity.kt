package com.example.simbahnjangkah

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import com.example.simbahnjangkah.ui.theme.SimbahNjangkahTheme
import kotlinx.coroutines.delay

class WelcomingActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SimbahNjangkahTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.primary
                ) {
//                    VectorImage(vectorResId = R.drawable.bg_welcoming)
                    ImageBackground(imageResId = R.drawable.bg_welcoming)
                    Footer()
                }
            }
        }
    }


    fun launchActivity() {
        val intent = Intent(this@WelcomingActivity, RegisterActivity::class.java)
        startActivity(intent)
        finish()
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
    fun Footer() {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp, 0.dp)

        ) {
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Meniti 5000 langkah setiap hari bukan hanya sekadar latihan, tapi juga kunci untuk menjaga kebugaran dan mengurangi risiko jatuh bagi para lansia.",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(0.dp, 24.dp),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.secondary
            )
            Button(
                onClick = {
                    launchActivity()
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(0.dp, 0.dp, 0.dp, 42.dp),
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.tertiary),
            ) {
                Text(
                    text = "Mulai Sekarang",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary
                )
            }

        }
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        SimbahNjangkahTheme {
//        Greeting3("Android")
        }
    }

}
