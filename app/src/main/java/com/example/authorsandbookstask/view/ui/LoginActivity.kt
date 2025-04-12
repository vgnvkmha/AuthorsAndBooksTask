package com.example.authorsandbookstask.view.ui

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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


class LoginActivity : ComponentActivity() {
    private var testString = "test"
    private val colors = listOf(Color.Cyan, Color.Blue, myPurple)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        lifecycleScope.launch {
//            allUsers = KtorClient.client.get("http://10.0.2.2:8080/all_users").body()
//        }
        setContent {
            Column(modifier = Modifier.fillMaxWidth()
                .padding(16.dp
            )) {
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
                    "text", onTextChange = {testString = it}
                )
                Spacer(modifier = Modifier.height(20.dp))
                CustomLoginWindow("password",
                    "text", onTextChange = {testString = it})
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