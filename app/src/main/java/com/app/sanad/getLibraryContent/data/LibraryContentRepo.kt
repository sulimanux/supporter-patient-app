package com.app.sanad.getLibraryContent.data

import android.util.Log
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import com.app.sanad.util.LIBRARY_CONTENTS
import com.app.sanad.util.log

// Repository for retrieving library content from local DB and Firebase
class LibraryContentRepo(
    private val firestore: FirebaseFirestore,
    private val libraryDao: LibraryDao,
) {

    // Get library content: prefer local DB, fallback to Firebase
    suspend fun getLibraryContent(): List<LibraryContent> {
        log("getLibraryContent")
        var contents = getContentFromRoom()
        log("before getLibraryContent , size => ${contents.size} ,  contents => $contents")
        // If local DB is empty, fetch from Firebase and cache it
        try {
            if (contents.isEmpty()) {
                log("TAG", "getLibraryContent: local DB empty, fetching Firebase")
                retrieveLibraryContentFromFirebase().let {
                    libraryDao.insertAll(it)
                    log("TAG", "insertAll: cached Firebase data in Room")
                    contents = it
                }
            }


        }catch (e: Exception){
            log( "getLibraryContent: Exception ${e.message}")
        }
        log("after getLibraryContent")
        return contents
    }

    // Get content from Room database
    private suspend fun getContentFromRoom() = libraryDao.getAll()

    // Fetch content from Firebase Realtime Database
    private suspend fun retrieveLibraryContentFromFirebase(): List<LibraryContent> {
        val snapshot = FirebaseDatabase.getInstance()
            .getReference(LIBRARY_CONTENTS)
            .get()
            .await()

        return snapshot.children.mapNotNull {
            it.getValue(LibraryContent::class.java)
        }
    }
}
