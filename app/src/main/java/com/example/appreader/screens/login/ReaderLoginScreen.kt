package com.example.appreader.screens.login

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.appreader.R
import com.example.appreader.components.EmailInput
import com.example.appreader.components.PasswordInput
import com.example.appreader.components.ReaderTitle
import com.example.appreader.components.SubmitButton
import com.example.appreader.navigation.ReaderScreens

@Composable
fun ReaderLoginScreen(
    navController: NavHostController,
    viewModel: LoginScreenViewModel = viewModel()
) {
    val context: Context = LocalContext.current
    val toastMessage by viewModel.showToast.observeAsState("")
    if (toastMessage.isNotEmpty()) {
        Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show()
        viewModel.clearToastMessage()
    }
    val showLoginForm = rememberSaveable {
        mutableStateOf(false)
    }

    Surface(Modifier.fillMaxSize()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            ReaderTitle()
            if (showLoginForm.value) {
                UserForm(loading = false, isCreateAccount = false) { email, password ->
                    viewModel.signInWithEmailAndPassword(email, password) {
                        navController.navigate(ReaderScreens.HomeScreen.name)
                    }
                }
            } else {
                UserForm(loading = false, isCreateAccount = true) { email, password ->
                    //firebase create account
                    viewModel.createUserWithEmailAndPassword(email, password) {
                        navController.navigate(ReaderScreens.HomeScreen.name)
                    }
                }
            }
        }
        Row(
            Modifier.padding(15.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            val clickableText = if (showLoginForm.value) "Create Account" else "Login"
            val staticText = if (showLoginForm.value) "New User?" else "Existing User?"
            Text(text = staticText)
            Text(text = clickableText, Modifier
                .clickable {
                    showLoginForm.value = !showLoginForm.value
                }
                .padding(start = 5.dp),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colors.secondaryVariant)
        }
    }
}

@Composable
fun UserForm(
    loading: Boolean = false,
    isCreateAccount: Boolean = false,
    onDone: (String, String) -> Unit = { email, password -> }
) {
    val email = rememberSaveable {
        mutableStateOf<String>("")
    }
    val password = rememberSaveable {
        mutableStateOf<String>("")
    }
    val passwordVisibility = rememberSaveable {
        mutableStateOf<Boolean>(false)
    }
    val passwordFocusRequest = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current
    val valid = remember(email.value, password.value) {
        email.value.isNotEmpty() && password.value.isNotEmpty()
    }
    val modifier: Modifier = Modifier
        .height(600.dp)
        .background(Color.White)
        .verticalScroll(rememberScrollState())

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        if (isCreateAccount) {
            Text(text = stringResource(R.string.create_account), Modifier.padding(4.dp))
        } else {
            Text("")
        }
        EmailInput(
            emailState = email, enabled = !loading, onAction = KeyboardActions {
                passwordFocusRequest.requestFocus()
            })
        PasswordInput(
            modifier = Modifier.focusRequester(passwordFocusRequest),
            passwordState = password,
            labelId = "Password",
            enabled = !loading,
            onAction = KeyboardActions {
                if (!valid) return@KeyboardActions
                keyboardController?.hide()
                onDone(email.value.trim(), password.value.trim())
            },
            passwordVisibility = passwordVisibility
        )
        SubmitButton(
            textId = if (isCreateAccount) "Create Account" else "Login",
            loading = loading,
            validInputs = valid
        ) {
            onDone(email.value.trim(), password.value.trim())
            keyboardController?.hide()
        }
    }
}



