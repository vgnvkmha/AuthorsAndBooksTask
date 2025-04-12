package com.example.authorsandbookstask.data

import androidx.lifecycle.LiveData

class MyRepository(private val dao: MyDao) {
    val readAllAuthors:LiveData<List<Author>> = dao.readAllAuthors()
    val readAllBooks:LiveData<List<Book>> = dao.readAllBooks()
    suspend fun addAuthor(name: String){
        val new = Author(name, 0)
        dao.addAuthor(new)
    }
    suspend fun addBook(book: Book) {
        dao.addBook(book)
    }
    fun getAuthorById(author:String) : Int {
        return dao.getAuthorById(author)
    }
    suspend fun getBookByAuthor(author:Int): List<Book>{
        return dao.getBooksByAuthor(author)
    }
    suspend fun updateAuthor(authorId:Int, name:String) {
        return dao.updateAuthor(authorId,name)

    }
    suspend fun deleteAll() {
        dao.deleteAllAuthors()
        dao.deleteAllBooks()
    }
    suspend fun getAuthorById(id:Int):Author {
        return dao.getAuthorById(id)
    }
    suspend fun updateBooks(newName:String, id:Int) {
        dao.updateBooks(newName, id)
    }
}