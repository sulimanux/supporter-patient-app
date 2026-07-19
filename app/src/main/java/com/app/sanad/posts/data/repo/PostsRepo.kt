package com.app.sanad.posts.data.repo

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import com.app.sanad.R
import com.app.sanad.notifications.data.entities.Notification
import com.app.sanad.notifications.data.entities.NotificationsEnum
import com.app.sanad.notifications.data.repos.NotificationsRepo
import com.app.sanad.posts.data.entity.Post
import com.app.sanad.posts.data.entity.Posts
import com.app.sanad.util.SUPPORTER
import com.app.sanad.util.POSTS
import com.app.sanad.util.SharedPreferencesManager
import com.app.sanad.util.log

class PostsRepo(
    private val firestore: FirebaseFirestore,
    private val sharedPreferences: SharedPreferencesManager,
    private val notificationsRepo: NotificationsRepo,
    private val context: android.content.Context

) {

    fun getUser() = sharedPreferences.getUserProfile()

    val countNotSeenBySupporter = MutableLiveData<Int>()

    suspend fun retrieveSharedList(): Posts? {
        val user = getUser()
        val email = if (user.typeOfUser == SUPPORTER) user.partnerEmail else user.email
        log(email!!)
        val result = firestore.collection(POSTS).document(email).get().await()
        return result.toObject(Posts::class.java)
    }


    suspend fun shareContent(post: Post): Boolean {
        try {
            val result = firestore.collection(POSTS).document(getUser().email!!).get().await()
            val posts: MutableList<Post> =
                if (result.exists()) {
                    result.toObject(Posts::class.java)?.posts ?: mutableListOf()
                } else {
                    mutableListOf()
                }
            post.postIndex = posts.size
            posts.add(post)
            firestore.collection(POSTS).document(getUser().email!!).set(Posts(posts)).await()
            sendNotificationToAllSupporter(post.listSupportersId)
            return true
        } catch (e: Exception) {
            return false
        }


    }

    private fun sendNotificationToAllSupporter(strings: List<String>? ) {
        strings?.forEach {
          val notification  = Notification(
              bodyAr = "منشور جديد من ${getUser().name}",
              bodyEn = "New post from ${getUser().name}",
              type = NotificationsEnum.Sharing.toString(),
              title = "New post"
          )
            notificationsRepo.sendNotification(userId = it, notification)
        }

    }


    suspend fun  addListenerToPosts(){
        try {
            firestore
                .collection(POSTS)
                .document(getUser().partnerEmail!!)
                .addSnapshotListener { value, error ->
                    handlePostsListener(value)
                }
        }catch (e: Exception){
            log(e.toString())
        }

    }

   suspend fun updateSupporterSeen(post: Post){

       try {
           val snapShot =   firestore.collection(POSTS)
               .document(getUser().partnerEmail!!).get().await()
           var posts = snapShot.toObject<Posts>(Posts::class.java)?.posts?.toMutableList()
           var index =   posts?.indexOf(post)
            posts!![index!!].seenList =   posts[index].seenList?.toMutableList()?.apply {
                add(getUser().email!!)
            }

           firestore.collection(POSTS)
               .document(getUser().partnerEmail!!).update("posts",posts).await()
           log("updateSupporterSeen => done ")

       }catch (e: Exception){
           log("updateSupporterSeen => $e ")

       }

    }



    private fun handlePostsListener(value: DocumentSnapshot?) {
        var counter = 0
        val posts = value?.toObject(Posts::class.java)?.posts?.reversed()
        posts?.let { post->
           post.forEachIndexed { index , it ->
                              if (it.supporters!!.contains(getUser().email)
                                  && it.seenList?.contains(getUser().email) == false){
                                  counter++
                        }
               countNotSeenBySupporter.value = counter


                 }



        }
    }


 }