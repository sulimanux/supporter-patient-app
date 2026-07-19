package com.app.sanad.getLibraryContent.presentaion

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import com.app.sanad.getLibraryContent.data.*
import com.app.sanad.util.*
import javax.inject.Inject

// ViewModel responsible for managing library content state and logic
@HiltViewModel
class LibraryViewModel @Inject constructor(
    private val libraryContentRepo: LibraryContentRepo,
    val depressionRepo: DepressionMisconceptionRepo,
    val sharedPreferences: SharedPreferencesManager
) : ViewModel() {

    // UI ready flag
    private val _isReadyDisplay = MutableLiveData<Boolean>()
    val isReadyDisplay: LiveData<Boolean> = _isReadyDisplay

    // Current selected content state
    private var currentCategory = ""
    lateinit var currentContent: LibraryContent
    // Cached content lists
    lateinit var mLibraryContentCustomized: List<LibraryContent>
    lateinit var mLibraryContentMostCommon: List<LibraryContent>

    // Fetch library content from repository
    fun retrieveLibraryContent() {
        viewModelScope.launch {
            try {
                log("retrieveLibraryContent")
                val data = libraryContentRepo.getLibraryContent()
                if (data.isNotEmpty()) {
                  val filteredList  = libraryContentsBasedReligion(data)
                    setLibraryContentMostCommon(filteredList)
                    setLibraryContentCustomized(filteredList)
                    _isReadyDisplay.value = true
                }
            } catch (e: Exception) {
                log("retrieveLibraryContent:Ex  ${e.message}")
            }
        }
    }


    // Return content list based on current category
    private fun getCurrentContents(): List<LibraryContent> =
        when (currentCategory) {
            COMMON_CONTENT -> mLibraryContentMostCommon
            else -> mLibraryContentCustomized
        }

    // Filter content by type and exclude the current content
    fun getContentsBasedType(type: String): List<LibraryContent> {

          val contentAfterExcludeCurrentContent =  getCurrentContents().filter {it !=  currentContent}
          return contentAfterExcludeCurrentContent.filter { it.type == type }
    }

    // Filter content based on user religion preference
    private fun libraryContentsBasedReligion(
        libraryContents: List<LibraryContent>?
    ): List<LibraryContent> {
        val isReligion = sharedPreferences.getUserProfile().religion!!
        return if (isReligion) libraryContents!!
        else libraryContents!!.filter { !it.religion!! }
    }

    // Select top 4 most viewed items if there
    private fun setLibraryContentMostCommon(libraryContents: List<LibraryContent>?) {
        mLibraryContentMostCommon =
            libraryContents!!
                .sortedByDescending { it.viewCount }
                .take(4)
    }

    // Build customized content list
    private fun setLibraryContentCustomized(libraryContents: List<LibraryContent>?) {
        mLibraryContentCustomized = libraryContents!!
    }

    // Reset UI ready state
    fun resetIsReadyDisplay() {
        _isReadyDisplay.value = false
    }



    // Update selected category
    fun setCurrentCategoryContent(content: String) {
        currentCategory = content
    }

}
