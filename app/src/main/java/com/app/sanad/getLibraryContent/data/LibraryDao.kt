package com.app.sanad.getLibraryContent.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.app.sanad.getLibraryContent.data.LibraryContent

@Dao
interface LibraryDao {

    @Insert
    suspend  fun insertAll(data:List<LibraryContent>)

    @Query("SELECT * FROM library_content")
    suspend  fun getAll(): List<LibraryContent>


}