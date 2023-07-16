package com.noobcode.gamebuddy.repo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.Query
import com.noobcode.gamebuddy.utils.FirebaseObject
import com.noobcode.gamebuddy.utils.ObjectModels
import com.noobcode.gamebuddy.utils.Utils

class UserRepo {

    fun lookForAPlayer(game: Int, userID: String): MutableLiveData<String?>{
        var res = MutableLiveData<String?>()

        FirebaseObject.fireStore.collection("lookup").whereNotEqualTo("userID", userID)/*.whereEqualTo("game", game)*/.get()
            .addOnSuccessListener { players ->

                if(players.size() > 0){
                    var playerID = players.toList()[Utils.getRandomNumber(0, players.size()-1)].getString("userID")
                    res.value = playerID
                } else{
                    res.value = null
                }
            }
            .addOnFailureListener {
                res.value = null
            }

        return res
    }

    fun setStatusForLookup(userID: String, status: Boolean, game: Int): MutableLiveData<Int>{

        var res: MutableLiveData<Int> = MutableLiveData<Int>(-99)

        if(status){
            //add the document from lookup

            val lookupMap = hashMapOf<String, Any>(
                "userID" to userID,
                "game" to game
            )

            FirebaseObject.fireStore.collection("lookup").document(userID).set(lookupMap)
                .addOnSuccessListener {
                    res.value = 1
                }
                .addOnFailureListener {
                    res.value = 0
                }

        }else{

            //delete the document from lookup
            FirebaseObject.fireStore.collection("lookup").document(userID).delete()
                .addOnSuccessListener {
                    res.value = 1
                }
                .addOnFailureListener {
                    res.value = 0
                }

        }

        return res
    }

    fun openChatBetweenUsers(documentName: String, userID1: String, userID2: String): MutableLiveData<Int>{

        var res: MutableLiveData<Int> = MutableLiveData<Int>(-99)

        val chatMap = hashMapOf<String, Any>(
            "userID1" to userID1,
            "userID2" to userID2
        )

        FirebaseObject.fireStore.collection("chats").document(documentName).set(chatMap)
            .addOnSuccessListener {
                res.value = 1
            }
            .addOnFailureListener {
                res.value = 0
            }

        return res
    }

    fun loadAllChatMessages(documentName: String): LiveData<MutableList<ObjectModels.ChatData>?> {
        var res = MutableLiveData<MutableList<ObjectModels.ChatData>?>()
        var messageList = mutableListOf<ObjectModels.ChatData>()

        FirebaseObject.fireStore.collection("chats").document(documentName).collection("messages").orderBy("timestamp", Query.Direction.ASCENDING).get()
            .addOnSuccessListener { messages ->
                for(message in messages){
                    messageList.add(message.toObject(ObjectModels.ChatData::class.java))
                }
                res.value = messageList
            }
            .addOnFailureListener {
                res.value = null
            }

        return res
    }

    fun listenToChatEvents(documentName: String, userID: String): LiveData<ObjectModels.ChatData?> {
        var res = MutableLiveData<ObjectModels.ChatData?>()

        FirebaseObject.fireStore.collection("chats").document(documentName).collection("messages").whereNotEqualTo("sender", userID).addSnapshotListener{ value, e->
            if (e != null) {
                res.value = null
            }else{
                //DocumentChange.Type.MODIFIED -> Log.d(TAG, "Modified city: ${dc.document.data}")
                //DocumentChange.Type.REMOVED -> Log.d(TAG, "Removed city: ${dc.document.data}")

                for (dc in value!!.documentChanges) {
                    if (dc.type == DocumentChange.Type.ADDED) {
                        res.value = dc.document.toObject(ObjectModels.ChatData::class.java)
                    }
                }
            }
        }

        return res
    }

    fun sendMessage(documentName: String, message: ObjectModels.ChatData) {
//        var res = MutableLiveData<ObjectModels.ChatData?>()

        FirebaseObject.fireStore.collection("chats").document(documentName).collection("messages").document().set(message)
            .addOnSuccessListener {

            }
            .addOnFailureListener {

            }

//        return res
    }

    fun checkInviteCode(code: String, userID: String): MutableLiveData<Int> {

        var res: MutableLiveData<Int> = MutableLiveData<Int>(-99)

        FirebaseObject.fireStore.collection("users").whereEqualTo("code", code).get()
            .addOnSuccessListener {
                if(it.isEmpty){
                    res.value = 0
                }else{

                    var userMap = mutableMapOf(
                        "code" to userID.substring(0,5),
                        "status" to 0
                    )
                    FirebaseObject.fireStore.collection("users").document(userID).set(userMap)
                        .addOnSuccessListener {
                            res.value = 1
                        }
                        .addOnFailureListener {
                            res.value = 0
                        }
                }
            }
            .addOnFailureListener {
                res.value = 0
            }

        return res

    }

    fun isUserVerified(userID: String): MutableLiveData<Int> {

        var res: MutableLiveData<Int> = MutableLiveData<Int>(-99)

        FirebaseObject.fireStore.collection("users").document(userID).get()
            .addOnSuccessListener {

                var user: ObjectModels.User? = it.toObject(ObjectModels.User::class.java)

                if(user == null){
                    res.value = 0
                }else{
                    if(user.code == userID.substring(0,5)) {
                        res.value = 1
                    }else{
                        res.value = 0
                    }
                }
            }
            .addOnFailureListener {
                res.value = 0
            }

        return res

    }

    fun submitSuggestion(userID: String, suggestion: String): MutableLiveData<Int> {

        var res: MutableLiveData<Int> = MutableLiveData<Int>(-99)

        var suggestionMap = mutableMapOf(
            "suggestion" to suggestion,
            "by" to userID
        )

        FirebaseObject.fireStore.collection("suggestion").document().set(suggestionMap)
            .addOnSuccessListener {
                res.value = 1
            }
            .addOnFailureListener {
                res.value = 0
            }

        return res

    }

    fun loadAnnouncements(): LiveData<MutableList<ObjectModels.Announcement>?> {
        var res = MutableLiveData<MutableList<ObjectModels.Announcement>?>()
        var announcementList = mutableListOf<ObjectModels.Announcement>()

        FirebaseObject.fireStore.collection("announcements").orderBy("timestamp", Query.Direction.DESCENDING).get()
            .addOnSuccessListener { announcements ->
                for(announcement in announcements){
                    announcementList.add(announcement.toObject(ObjectModels.Announcement::class.java))
                }
                res.value = announcementList
            }
            .addOnFailureListener {
                res.value = null
            }

        return res
    }

    fun loadAllMessages(userID: String): LiveData<MutableList<ObjectModels.Chat>?> {
        var res = MutableLiveData<MutableList<ObjectModels.Chat>?>()
        var chatList = mutableListOf<ObjectModels.Chat>()

        FirebaseObject.fireStore.collection("chats").whereEqualTo("userID1", userID).get()
            .addOnSuccessListener { chats ->
                for(chat in chats){
                    chatList.add(chat.toObject(ObjectModels.Chat::class.java))
                }

                FirebaseObject.fireStore.collection("chats").whereEqualTo("userID2", userID).get()
                    .addOnSuccessListener { chats ->
                        for (chat in chats) {
                            chatList.add(chat.toObject(ObjectModels.Chat::class.java))
                        }

                        res.value = chatList
                    }
                    .addOnFailureListener {
                        res.value = null
                    }

            }
            .addOnFailureListener {
                res.value = null
            }

        return res
    }

}