package com.example.authorsandbookstask.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MyViewModel(application: Application): AndroidViewModel(application) {
    private val repository: MyRepository
    val readAllAuthors: LiveData<List<Author>>
    val readAllBooks: LiveData<List<Book>>

    init {
        val myDao = MyDatabase.getDatabase(application).myDao()
        repository = MyRepository(myDao)
        readAllAuthors = repository.readAllAuthors
        readAllBooks = repository.readAllBooks

    }

    fun addAuthor(user: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addAuthor(user)
        }

    }
    fun addBook(book: Book) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addBook(book)
        }
    }
    suspend fun getIdByAuthor(author:String) : Int {
        return repository.getAuthorById(author)
    }

    suspend fun getBooksByAuthor(author: Int):List<Book> {
        return repository.getBookByAuthor(author)
    }
    suspend fun deleteAll() {
        repository.deleteAll()
    }
    suspend fun updateAuthor(id:Int, name:String) {
        repository.updateAuthor(id, name)
    }
    suspend fun getAuthorById(id:Int):Author  {
        return repository.getAuthorById(id)
    }
    suspend fun updateBooks(newName:String, id:Int) {
        repository.updateBooks(newName, id)
    }
}