package com.example.appreader.screens.login

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appreader.model.MUser
import com.example.appreader.utils.Constants.LOG_TAG
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class LoginScreenViewModel : ViewModel() {
    val loadingState = MutableStateFlow(LoadingState.IDLE)
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val _loading = MutableLiveData<Boolean>(false)
    val loading: LiveData<Boolean> = _loading

    private val _showToast = MutableLiveData<String>("")
    val showToast: LiveData<String> = _showToast

    fun createUserWithEmailAndPassword(email: String, password: String, home: () -> Unit) {
        viewModelScope.launch {
            if (_loading.value == false) {
                _loading.value = true
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val displayName = task.result.user?.email?.split("@")?.get(0)
                            val uuid = auth.currentUser?.uid
                            createUser(displayName, uuid)
                            _loading.value = false
                            Log.d(LOG_TAG, "createUserWithEmailAndPassword: success")
                            home()
                        } else {
                            _loading.value = false
                            Log.d(LOG_TAG, "createUserWithEmailAndPassword: failed")
                        }
                    }.addOnFailureListener {
                        _loading.value = false
                        _showToast.value = it.message.toString()
                        Log.d(LOG_TAG, "createUserWithEmailAndPassword: ${it.message}")
                    }
            }
        }

    }

    private fun createUser(userName: String?, uuid: String?) {
        if (userName !== null && uuid != null) {
            val user = MUser(
                userId = uuid,
                displayName = userName,
                avatarURL = "",
                quote = "Life Is Great",
                profession = "Android Developer",
                id = null
            ).toMap()
            FirebaseFirestore.getInstance().collection("users")
                .add(user)
        }
    }

    fun signInWithEmailAndPassword(email: String, password: String, home: () -> Unit) {
        viewModelScope.launch {
            try {
                _loading.value = true
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            _loading.value = false
                            Log.d(LOG_TAG, "signInWithEmailAndPassword: success")
                            home()
                        } else {
                            _loading.value = false
                            Log.d(LOG_TAG, "signInWithEmailAndPassword: failed")
                        }
                    }
            } catch (e: Exception) {
                Log.d(LOG_TAG, "signInWithEmailAndPassword: ${e.message}")
            }
        }

    }

    fun clearToastMessage() {
        _showToast.value = ""
    }
}