package com.app.sanad.chatting.data.repo

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import kotlinx.coroutines.tasks.await
import com.app.sanad.chatting.data.entity.Chatting
import com.app.sanad.chatting.data.entity.Message
import com.app.sanad.chatting.data.entity.MetaDataMessages
import com.app.sanad.notifications.data.entities.Notification
import com.app.sanad.notifications.data.entities.NotificationsEnum
import com.app.sanad.notifications.data.repos.NotificationsRepo
import com.app.sanad.notifications.presentation.NotificationsFragment
import com.app.sanad.util.SUPPORTER
import com.app.sanad.util.CHATS
import com.app.sanad.util.SharedPreferencesManager
import com.app.sanad.util.log

class ChattingRepo(
    private val firestore: FirebaseFirestore,
    private val sharedPreferences: SharedPreferencesManager,
    private val     notificationsRepo: NotificationsRepo
    ) {
    private var chatListener: ListenerRegistration? = null
    private var messagesListener: ListenerRegistration? = null
    fun user() = sharedPreferences.getUserProfile()
    fun isUserSupporter()= user().typeOfUser == SUPPORTER
    fun hasPartner() = user().hasPartner
     var chattingList = MutableLiveData<List<Chatting>>()
     var messagesList = MutableLiveData<List<Message>>()
    val unseenMessagesCount = MutableLiveData<Int>(0)

   private fun chatIdPrefix(): String {
       // fdgs
        return  if (user().typeOfUser == SUPPORTER){
            user().partnerId!!.take(4)  // userId patient
        }else{
            user().id!!.take(4) // userId
        }
    }

     fun retrieveChattingListForSupporter(): ListenerRegistration {

         log("retrieveChattingListForSupporter")

      return   firestore.collection(CHATS).document(getChatId(user().partnerId!!))
            .addSnapshotListener { documentSnap, error ->
            if (error != null) {
                return@addSnapshotListener
            }
            if (documentSnap != null ) {
                log(" retrieveChattingListForSupporter documentSnap => $documentSnap")
                val list = mutableListOf<Chatting>()
                val chatting = documentSnap.toObject(Chatting::class.java)
                log(" retrieveChattingListForSupporter chatting => $chatting")
                if (chatting != null){
                    list.add(chatting)
                }
                chattingList.value = list
                updateUnseenMessagesCount(list)
            } else {
                log(" retrieveChattingListForSupporter documentSnap null")
                chattingList.value = mutableListOf()
            }
        }
    }

     fun listenToChatting(){
         chatListener?.remove()  // remove old listener
         chatListener = if (isUserSupporter()) {
             retrieveChattingListForSupporter()
         } else {
             retrieveChattingListForUser()
         }
    }


    /**
     * chatId  = userId + careId
     * one to many   id = user + supporter
     *  user => userid
     *  supporter => userIdPartner + careId
     */

      fun retrieveChattingListForUser(): ListenerRegistration {
        log("retrieveChattingListForUser")

        val chatIdPrefix = chatIdPrefix()
        log("chatIdPrefix => $chatIdPrefix")
     return   firestore.collection(CHATS)
            .orderBy("__name__")
            .startAt(chatIdPrefix)
            .endAt(chatIdPrefix + '\uf8ff')

            .addSnapshotListener { querySnapshot, error ->
                if (error != null) {
                    return@addSnapshotListener
                }
                if (querySnapshot != null && !querySnapshot.isEmpty) {
                    val list = mutableListOf<Chatting>()
                    for (document in querySnapshot.documents) {
                        val chatting = document.toObject(Chatting::class.java)
                        list.add(chatting!!)
                    }
                    chattingList.value = list
                    updateUnseenMessagesCount(list)
                } else {
                    chattingList.value = mutableListOf()
                }
            }
    }
    fun updateUnseenMessagesCount(chattings: List<Chatting>) {
           var count = 0
        chattings.forEach {chatting->
        val isSeen =    chatting.messages?.last().let {
                if (isUserSupporter()) it?.seenBySupporter
                else it?.seenByPatient
            }
            log("isSeen => $isSeen")
            if (!isSeen!!)  count++
        }

        unseenMessagesCount.value = count
    }



    fun listenToMessages(partnerId:String){

       val chatId = getChatId(partnerId)
        log("chatId =>  $chatId")
        firestore.collection(CHATS).document(chatId)
            .addSnapshotListener { documentSnapshot, error ->
                if (error != null) {
                    return@addSnapshotListener
                }

                if ( documentSnapshot != null && documentSnapshot.exists()  ) {
                    val chatting =
                        documentSnapshot.toObject(Chatting::class.java)?.messages ?: mutableListOf()
                    messagesList.value = chatting


                } else {
                    messagesList.value = emptyList()

                }
            }
    }

    fun clearData() {
        log("clearData catting repo ")
        chattingList = MutableLiveData<List<Chatting>>()
        messagesList = MutableLiveData<List<Message>>()
        unseenMessagesCount.value = 0
        chatListener?.remove()
        messagesListener?.remove()
    }


    fun updateSeenMessages(partnerId: String, index: Int) {
        val chatId = getChatId(partnerId)
        val chat = chattingList.value?.get(index) ?: return
       chat.messages?.lastOrNull().apply {
            if (isUserSupporter()) this?.seenBySupporter = true else this?.seenByPatient = true
        }
       firestore.collection(CHATS).document(chatId).set(chat)
    }



    private fun getChatId(partnerId:String): String {
    // id =  patient + care
        val userId = user().id!!.take(4)   // pati
        var partnerId = partnerId.take(4) // care

        return if (isUserSupporter()){
  //  id =  partnerId +  userId
            partnerId = user().partnerId!!.take(4) //  aEoc
            partnerId + userId
        } else {
            // id = userId +  partnerId
            userId + partnerId
        }

    }

   suspend fun sendMessage(message: Message, metaDataMessages: MetaDataMessages, partnerId:String ) {
        val chatId = getChatId(partnerId)
        val nameUser = sharedPreferences.getUserProfile().name
        val messagesList = messagesList.value?.toMutableList() ?: mutableListOf()
         messagesList.add(message)
        val chatting = Chatting(metaDataMessages, messagesList)
        firestore.collection(CHATS).document(chatId).set(chatting).await()
        val notification = Notification(
            bodyAr = "رسالة جديدة من ${nameUser}",
            bodyEn = "New message from ${nameUser}",
            type = NotificationsEnum.Chat.toString(),
            title = "New message"
        )
        notificationsRepo.sendNotification(partnerId,notification)
    }

}