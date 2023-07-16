package com.noobcode.gamebuddy.utils

import java.math.BigInteger
import java.security.MessageDigest
import kotlin.math.abs

object Utils {

    fun getRandomNumber(low: Int, high: Int): Int{
        return (low..high).random()
    }

    fun getAStringFrom2Users(userID1: String, userID2: String): String{
        var totalSum1 = 0
        var totalSum2 = 0
        for (char in userID1) {
           totalSum1 += char.toByte().toInt()
        }

        for (char in userID2) {
            totalSum2 += char.toByte().toInt()
        }

        val subString = abs(totalSum2 - totalSum1).toString()
        val md = MessageDigest.getInstance("MD5")
        return BigInteger(1, md.digest(subString.toByteArray())).toString(16).padStart(32, '0')
    }

}