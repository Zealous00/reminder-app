package com.fekete.david.reminderapp.ui.login

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.fekete.david.reminderapp.viewmodel.AuthViewModel
import com.fekete.david.reminderapp.viewmodel.UserLoginStatus

@Composable
fun LoginScreen(
    modifier: Modifier,
    navController: NavController,
    context: Context,
    authViewModel: AuthViewModel
) {


    val userEmail = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val pincode = remember { mutableStateOf("") }
    val isPinCodeLogin = remember { mutableStateOf(false) }

    val loginStatus by authViewModel.userLoginStatus.collectAsState()

    LaunchedEffect(key1 = loginStatus) {
        when (loginStatus) {
            is UserLoginStatus.Failure -> {
                shortToast(context, "Unable to login!")
            }
            UserLoginStatus.Succesful -> {
//                shortToast(context, "Login successful!")
                navController.navigate("home")
            }
            null -> {}
        }
    }

    Column(
        modifier = modifier.padding(20.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = rememberVectorPainter(image = Icons.Filled.Notifications),
            contentDescription = "login_image",
            modifier = Modifier
                .fillMaxWidth()
                .size(150.dp),
        )
        Spacer(
            modifier = Modifier.height(10.dp)
        )
        if (isPinCodeLogin.value) {
            PinCodeLoginOption(
                pincode = pincode,
                isPinCodeLogin = isPinCodeLogin
            )
        } else {
            EmailPasswordOption(
                userEmail = userEmail,
                password = password,
                isPinCodeLogin = isPinCodeLogin
            )
        }


        Spacer(
            modifier = Modifier.height(16.dp)
        )
        Button(
            onClick = {
                if (isPinCodeLogin.value) {
//                    if (pincode.value.equals(savedUser.value?.pincode)) {
//                        navController.navigate("home")
//                    } else {
//                        shortToast(context, "Pin code is not correct!")
//                    }
                } else {
                    when {
                        userEmail.value.isBlank() || password.value.isBlank() -> {
                            shortToast(context, "Please fill out all the fields!")
                        }
                        else -> {
                            authViewModel.performLogin(userEmail.value, password.value)
                        }
                    }

                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(corner = CornerSize(50.dp))
        ) {
            Text(text = "Login")
        }
        Spacer(
            modifier = Modifier.height(16.dp)
        )
        Text(
            text = "Don't have an account?",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        Spacer(
            modifier = Modifier.height(4.dp)
        )
        Text(
            text = "Register now!",
            modifier = Modifier
                .fillMaxWidth()
                .clickable { navController.navigate("registration") },
            color = Color.Blue,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun PinCodeLoginOption(
    pincode: MutableState<String>,
    isPinCodeLogin: MutableState<Boolean>,
    pinCodeSize: Int = 4,
) {
//    Text(text = "this is the other one")
    BasicTextField(
        value = pincode.value,
        onValueChange = { pc ->
            if (pc.length <= pinCodeSize) {
                pincode.value = pc
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
        decorationBox = {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                repeat(pinCodeSize) { index ->
                    PinCodeChar(
                        index = index,
                        text = pincode.value
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
    )
    Spacer(
        modifier = Modifier.height(24.dp)
    )
    OutlinedButton(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(corner = CornerSize(50.dp)),
        onClick = { togglePinCodeLogin(isPinCodeLogin) },
        border = BorderStroke(2.dp, color = MaterialTheme.colors.primary)
    )
    {
        Text(
            text = "Login using user credentials",
            style = TextStyle(
                color = Color.White,
                textAlign = TextAlign.Center
            )
        )
    }
}

@Composable
fun PinCodeChar(index: Int, text: String) {
    val char = when {
        index >= text.length -> ""
//        else -> text[index].toString()
        else -> "*"
    }
    Text(
        modifier = Modifier
            .width(40.dp)
            .border(border = BorderStroke(1.dp, Color.White), RoundedCornerShape(8.dp))
            .padding(2.dp),
        text = char,
        style = MaterialTheme.typography.h4,
        textAlign = TextAlign.Center
    )
}

@Composable
fun EmailPasswordOption(
    userEmail: MutableState<String>,
    password: MutableState<String>,
    isPinCodeLogin: MutableState<Boolean>
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = userEmail.value,
        onValueChange = { text -> userEmail.value = text },
        label = { Text(text = "Email address") },
        shape = RoundedCornerShape(corner = CornerSize(50.dp))
    )
    Spacer(
        modifier = Modifier.height(10.dp)
    )
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = password.value,
        onValueChange = { pwd -> password.value = pwd },
        label = { Text(text = "Password") },
        visualTransformation = PasswordVisualTransformation(),
        shape = RoundedCornerShape(corner = CornerSize(50.dp))
    )

    Spacer(
        modifier = Modifier.height(10.dp)
    )
    Spacer(
        modifier = Modifier.height(24.dp)
    )
    OutlinedButton(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(corner = CornerSize(50.dp)),
        onClick = { togglePinCodeLogin(isPinCodeLogin) },
        border = BorderStroke(2.dp, color = MaterialTheme.colors.primary)
    )
    {
        Text(
            text = "Login using PIN code",
            style = TextStyle(
                color = Color.White,
                textAlign = TextAlign.Center
            )
        )
    }
}

fun togglePinCodeLogin(pinCodeLogin: MutableState<Boolean>) {
    pinCodeLogin.value = !pinCodeLogin.value
}


fun shortToast(context: Context, message: String) {
    Toast.makeText(
        context,
        message,
        Toast.LENGTH_SHORT
    ).show()
}
