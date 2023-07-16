package com.noobcode.gamebuddy.utils

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

object FirebaseObject {
    lateinit var auth: FirebaseAuth
    lateinit var currentUser: FirebaseUser
    lateinit var fireStore: FirebaseFirestore
}