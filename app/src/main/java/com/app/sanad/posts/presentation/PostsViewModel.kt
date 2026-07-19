package com.app.sanad.posts.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import com.app.sanad.posts.data.repo.PostsRepo
import com.app.sanad.posts.data.entity.Post
import com.app.sanad.users.patient.supporters.data.repos.SupportersRepo
import com.app.sanad.util.SUPPORTER
import com.app.sanad.util.SharedPreferencesManager
import com.app.sanad.util.log
import javax.inject.Inject

@HiltViewModel
class PostsViewModel @Inject constructor(
    private val supportersRepo: SupportersRepo,
    private val postsRepo: PostsRepo,
    val sharedPreferences: SharedPreferencesManager,
) : ViewModel() {


    private val _posts = MutableLiveData<List<Post>?>()
    val posts: LiveData<List<Post>?>
        get() = _posts


    val user = postsRepo.getUser()
    val supportersProfile = supportersRepo.supportersProfile

    private val _statusSharing = MutableLiveData<Boolean>()
    val statusSharing: LiveData<Boolean> get() = _statusSharing


    fun sharePost(post: Post) {
        try {
            viewModelScope.launch {
                postsRepo.shareContent(post)
                _statusSharing.value = true
            }
        } catch (e: Exception) {
            _statusSharing.value = false

        }
    }

    fun retrieveSupporters() {
        viewModelScope.launch {
            supportersRepo.retrievePartnersIds(supportersRepo.userProfile().id!!)
        }
    }

    fun retrievePostsRemotely() {
        viewModelScope.launch {
            val data = postsRepo.retrieveSharedList()
            if (data != null) {
                log("data is not null")
                filterData(data.posts)
            }else{
                log("data is null")
                _posts.value = null
            }
        }
    }

    private fun filterData(posts: MutableList<Post>?) {
        log("user type is ${user.typeOfUser}")
     if (user.typeOfUser == SUPPORTER){
         log("user type is supporter")
         val list = posts?.filter {isContainUser(it.supporters!!,user.email!!)}
         _posts.value = list
     }else{
         log("user type is patient")
         _posts.value = posts
     }
    }

    private fun isContainUser(list: List<String>, email: String): Boolean {
        list.forEach {
            if (it == email) {
                return true
            }
        }
        return false
    }

    fun updateSupporterSeen(post: Post) {
       try {
           viewModelScope.launch {
               postsRepo.updateSupporterSeen(post)
           }
       }catch (e: Exception){

       }
    }

}