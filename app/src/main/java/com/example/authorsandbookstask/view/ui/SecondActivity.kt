package com.example.authorsandbookstask.view.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.authorsandbookstask.data.Book
import com.example.authorsandbookstask.data.MyViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class SecondActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val authorName = intent.getStringExtra("author_name") ?: "UNDEFINED"
        val newViewModel = MyViewModel(application)

        setContent {
            val booksState = remember { mutableStateOf<List<Book>>(emptyList()) }
            val newName = remember { mutableStateOf(authorName) }

            // Загружаем книги при старте
            LaunchedEffect(Unit) {
                val id = withContext(Dispatchers.IO) {
                    newViewModel.getIdByAuthor(authorName)
                }
                val books = withContext(Dispatchers.IO) {
                    newViewModel.getBooksByAuthor(id)
                }
                booksState.value = books
            }

            Column(modifier = Modifier.padding(16.dp)) {
                MyRow(
                    author = authorName,
                    books = booksState.value,
                    text = newName.value,
                    onTextChange = { newName.value = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = {
                        CoroutineScope(Dispatchers.IO).launch {
                            val id = newViewModel.getIdByAuthor(authorName)
                            newViewModel.updateAuthor(id, newName.value)
                            newViewModel.updateBooks(newName.value, id)
                        }
                        val intent = Intent(this@SecondActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish() // закрыть текущую активность
                    },
                    modifier = Modifier
                        .height(48.dp)
                        .fillMaxWidth()
                ) {
                    Text("Update Author")
                }
            }
        }
    }
}
@Composable
fun MyRow(
    author: String,
    books: List<Book>,
    text: String,
    onTextChange: (String) -> Unit
) {
    Column {
        TextField(
            value = text,
            onValueChange = onTextChange,
            placeholder = { Text("Author") },
            modifier = Modifier.fillMaxWidth()
        )
        Text(text = "Автор: $author", modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(16.dp))
        Text("Книги:")
        LazyColumn(modifier = Modifier.fillMaxWidth()) {
            items(books) { book ->
                Text(text = "${book.name}, ID: ${book.authorsId}")
            }
        }
    }
}