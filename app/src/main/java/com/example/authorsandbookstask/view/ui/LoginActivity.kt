package com.example.authorsandbookstask.view.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.lifecycleScope
import com.example.authorsandbookstask.data.KtorClient
import io.ktor.client.request.get
import kotlinx.coroutines.launch


class LoginActivity : ComponentActivity() {
    private val colors = listOf(Color.Cyan, Color.Blue, myPurple)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        lifecycleScope.launch {
//            allUsers = KtorClient.client.get("http://10.0.2.2:8080/all_users").body()
//        }
        setContent {
            val intentToMain = Intent(this@LoginActivity, MainActivity::class.java)
            Column(modifier = Modifier.fillMaxWidth()
                .padding(16.dp
            )) {
                val username = remember { mutableStateOf("") }
                val password = remember { mutableStateOf("") }
                Text(text = "Login",
                    fontSize = 40.sp,
                    fontStyle = FontStyle.Italic,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    style = TextStyle(
                        brush = Brush.linearGradient(
                            colors = colors
                        )
                    )
                )
                CustomLoginWindow("username",
                    username.value, onTextChange = {username.value = it}
                )
                Spacer(modifier = Modifier.height(20.dp))
                CustomLoginWindow("password",
                    password.value, onTextChange = {password.value = it})
//                Button(enabled = true,
//                    onClick = {
//                        lifecycleScope.launch {
//                            val validUser = KtorClient.client.get("http://10.0.2.2:8080/user/${}")
//                        }
//                    }) { }
            }

        }
    }
}


@Composable
fun CustomLoginWindow(
    label: String,
    text: String,
    onTextChange: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(
                width = 1.dp,
                color = Color.LightGray,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(16.dp)
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        TextField(
            value = text,
            onValueChange = onTextChange,
            label = { Text("Enter $label") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
    }
}