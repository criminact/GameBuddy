package com.noobcode.gamebuddy.utils

import com.noobcode.gamebuddy.R

object ObjectModels {

    class User {
        var code: String = ""
        var status: Int = 0
    }

    class Chat {
        var userID1: String = ""
        var userID2: String = ""
    }

    class ChatData() {
        var message: String = ""
        var sender: String = ""
        var timestamp: Long = 0L
        var seenStatus: Boolean = false
    }

    class Announcement {
        var title: String = ""
        var description: String = ""
        var image_url: String = ""
        var timestamp: Long = 0L
    }

    var gameNameToInt: HashMap<String, Int> = HashMap()

    var avatarForUsers = mutableListOf<Int>()

    init {
        gameNameToInt["Valorant"] = 0
        gameNameToInt["Rainbow 6 Siege"] = 1
        gameNameToInt["CS:GO"] = 2
        gameNameToInt["Fortnite"] = 3

        avatarForUsers.add(R.drawable.avatar1)
        avatarForUsers.add(R.drawable.avatar2)
        avatarForUsers.add(R.drawable.avatar3)
        avatarForUsers.add(R.drawable.avatar4)
        avatarForUsers.add(R.drawable.avatar5)
        avatarForUsers.add(R.drawable.avatar6)
        avatarForUsers.add(R.drawable.avatar7)
        avatarForUsers.add(R.drawable.avatar8)

    }

}