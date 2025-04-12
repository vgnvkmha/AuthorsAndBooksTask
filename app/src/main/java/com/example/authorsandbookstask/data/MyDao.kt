package com.example.authorsandbookstask.data

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query



@Dao
interface MyDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addAuthor(author: Author)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addBook(book: Book)

    @Query("SELECT * FROM authors_table ORDER BY id ASC")
    fun readAllAuthors(): LiveData<List<Author>>

    @Query("SELECT * FROM books_table ORDER BY author ASC")
    fun readAllBooks(): LiveData<List<Book>>

    @Query("SELECT id FROM authors_table WHERE name = :author LIMIT 1")
    fun getAuthorById(author: String): Int

    @Query("SELECT * FROM books_table WHERE authorsId = :authorsId")
    fun getBooksByAuthor(authorsId:Int): List<Book>

    @Query("DELETE FROM authors_table")
    suspend fun deleteAllAuthors()

    @Query("DELETE FROM books_table")
    suspend fun deleteAllBooks()

    @Query("SELECT * FROM authors_table WHERE id=:id LIMIT 1")
    suspend fun getAuthorById(id:Int)  : Author

    @Query("UPDATE authors_table SET name = :name WHERE id = :authorId")
    suspend fun updateAuthor(authorId:Int, name:String)

    @Query("UPDATE books_table SET author=:newName WHERE authorsId=:id")
    suspend fun updateBooks(newName:String, id:Int)

}