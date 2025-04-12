package com.example.authorsandbookstask.view.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import com.example.authorsandbookstask.data.Author
import com.example.authorsandbookstask.data.Book
import com.example.authorsandbookstask.data.KtorClient
import com.example.authorsandbookstask.data.MyViewModel
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable


@Suppress("SpellCheckingInspection")
class MainActivity : ComponentActivity() {
    private lateinit var myViewModel: MyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        myViewModel = MyViewModel(application)

        setContent {
            val authorsList = remember { mutableStateListOf<Author>() }
            val booksList = remember { mutableStateListOf<Book>() }
            val isAppLaunched = remember { intent.action == Intent.ACTION_MAIN && intent.hasCategory(Intent.CATEGORY_LAUNCHER) }
            // Set up observers once
            LaunchedEffect(Unit) {
                if (isAppLaunched) {
                    runBlocking {
                        myViewModel.deleteAll()
                    }
                }
                 myViewModel.readAllAuthors.observe(this@MainActivity) { res ->
                    authorsList.clear()
                    authorsList.addAll(res)
                }

                myViewModel.readAllBooks.observe(this@MainActivity) { res ->
                    booksList.clear()
                    booksList.addAll(res)
                }
            }
            val intentLogin = Intent(this, LoginActivity::class.java)
            // UI остаётся тем же
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                Button(enabled = true,
                    onClick = {
                        startActivity(intentLogin)
                    }, modifier = Modifier.fillMaxWidth()) {
                    Text("Login page")
                }
                val authorName = remember { mutableStateOf("") }
                val bookName = remember { mutableStateOf("") }

                TextField(
                    value = authorName.value,
                    onValueChange = { new -> authorName.value = new },
                    placeholder = { Text("Author") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                )

                TextField(
                    value = bookName.value,
                    onValueChange = { new -> bookName.value = new },
                    placeholder = { Text("Book") },
                    modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
                )

                Button(
                    onClick = {
                        lifecycleScope.launch {
                            try {
                                val authorExists = authorsList.any { it.name == authorName.value }
                                val authorId: Int
                                
                                if (authorExists) {
                                    authorId = myViewModel.getIdByAuthor(authorName.value)
                                } else {
                                    myViewModel.addAuthor(authorName.value)
                                    delay(500) // Give time for the database to update
                                    authorId = myViewModel.getIdByAuthor(authorName.value)
                                }

                                val bookExists = booksList.any {
                                    it.name == bookName.value && it.authorsId == authorId
                                }

                                if (!bookExists) {
                                    val newBook = Book(
                                        name = bookName.value,
                                        authorsId = authorId,
                                        author = authorName.value
                                    )
                                    myViewModel.addBook(newBook)
                                } else {
                                    Toast.makeText(this@MainActivity, "Такая книга у этого автора уже существует!", Toast.LENGTH_SHORT).show()
                                }
                            } catch (e: Exception) {
                                Toast.makeText(this@MainActivity, "Ошибка: ${e.message}", Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Добавить")
                }

                Spacer(modifier = Modifier.height(16.dp))
                var itemToDelete by remember { mutableStateOf<Any?>(null) }
                var isDialogOpen by remember { mutableStateOf(false) }

                Text(text = "Авторы")
                LazyColumn(modifier = Modifier.fillMaxWidth().height(200.dp)) {
                    items(authorsList) { author ->
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                                .clickable {
                                    val intent = Intent(this@MainActivity, SecondActivity::class.java)
                                        .putExtra("author_name", author.name)
                                    startActivity(intent)
                                }
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${author.name} (ID: ${author.id})",
                                    modifier = Modifier.weight(1f)
                                )
                                IconButton(onClick = {
                                    itemToDelete = author
                                    isDialogOpen = true
                                }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Удалить")
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Книги")
                LazyColumn(modifier = Modifier.fillMaxWidth().height(300.dp)) {
                    items(booksList) { book ->
                        Card(
                            modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${book.name} - ${book.author} (ID: ${book.authorsId})",
                                    modifier = Modifier.weight(1f)
                                )
                                IconButton(onClick = {
                                    itemToDelete = book
                                    isDialogOpen = true
                                }) {
                                    Icon(Icons.Default.Delete, contentDescription = "Удалить")
                                }
                            }
                        }
                    }
                }

                if (isDialogOpen) {
                    AlertDialog(
                        onDismissRequest = {
                            isDialogOpen = false
                        },
                        title = { Text("Удалить?") },
                        text = { Text("Вы уверены, что хотите удалить? Это действие нельзя отменить.") },
                        confirmButton = {
                            TextButton(onClick = {
                                lifecycleScope.launch {
                                    try {
                                        when (val item = itemToDelete) {
                                            is Author -> {
                                                authorsList.remove(item)
                                                booksList.removeAll { it.authorsId == item.id }
                                            }
                                            is Book -> {
                                                booksList.remove(item)
                                            }
                                        }
                                        isDialogOpen = false
                                        itemToDelete = null
                                    } catch (e: Exception) {
                                        Toast.makeText(this@MainActivity, "Ошибка удаления: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }) {
                                Text("Удалить")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = {
                                isDialogOpen = false
                                itemToDelete = null
                            }) {
                                Text("Отмена")
                            }
                        }
                    )
                }
            }
        }
    }

//    override fun onResume() {
//        super.onResume()
//
//        val passedId = intent.getStringExtra("authorsId")?.toIntOrNull()
//        passedId?.let { id ->
//            lifecycleScope.launch {
//                try {
//                    val updatedAuthor = myViewModel.getAuthorById(id)
//                    // Обновляем автора в списке (если уже был добавлен)
//                    val index = myViewModel.readAllAuthors.value?.indexOfFirst { it.id == id }
//                    if (index != null && index >= 0) {
//                        myViewModel.getBooksByAuthor(index)
//                    }
//                } catch (e: Exception) {
//                    Toast.makeText(this@MainActivity, "Ошибка обновления: ${e.message}", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }
//    }
}