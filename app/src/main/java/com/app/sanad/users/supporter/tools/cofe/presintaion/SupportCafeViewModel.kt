package com.app.sanad.users.supporter.tools.cofe.presintaion

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.scopes.ViewModelScoped
import com.app.sanad.users.supporter.tools.cofe.data.repo.Repository
import com.app.sanad.util.SharedPreferencesManager
import javax.inject.Inject

@HiltViewModel

class SupportCafeViewModel @Inject constructor(
   val sharedPreferences: SharedPreferencesManager,

    private val repository: Repository): ViewModel()  {


    private  val _selectedText = MutableLiveData<String>()
    val selectedText: LiveData<String> = _selectedText

    var selectedItem: Int = 0


    fun getPhrases(context: Context) = repository.getPhrases(context)

    fun  setSelectedText(text: String){
        _selectedText.value = text

    }
}