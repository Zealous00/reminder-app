package com.fekete.david.reminderapp.ui.login

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.datastore.preferences.core.Preferences
import androidx.navigation.NavController
import com.fekete.david.reminderapp.data.entitiy.User
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    modifier: Modifier,
    navController: NavController,
    context: ProvidableCompositionLocal<Context>
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    val dataStore = StoreUserCredentials(context = context)

    val userName = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val pincode = remember { mutableStateOf("") }
    val isPinCodeLogin = remember { mutableStateOf(false) }

    val savedUser = dataStore.getUserFromDataStore.collectAsState(initial = User("", "", "", "", ""))

    Column(
        modifier = modifier.padding(20.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
//        Image(
//            painter = painterResource(id = R.drawable.ic_launcher_foreground),
//            contentDescription = "login_image",
//            modifier = Modifier.fillMaxWidth(),
//            alignment = Alignment.Center
//        )

        Icon(
            painter = rememberVectorPainter(image = Icons.Filled.Person),
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
            UserNamePasswordOption(
                userName = userName,
                password = password,
                isPinCodeLogin = isPinCodeLogin
            )
        }


        Spacer(
            modifier = Modifier.height(30.dp)
        )
        Button(
            onClick = {
//                scope.launch {
//                    dataStore.saveUserCredentials(
//                        User(
//                            username = userName.value,
//                            password = password.value,
//                            phoneNumber = "+420911234567",
//                            pincode = "5555"
//                        )
//                    )
//                }
                if (isPinCodeLogin.value) {
                    if (pincode.value.equals(savedUser.value?.pincode)) {
                        navController.navigate("home")
                    }
                } else {
                    if (userName.value.equals(savedUser.value?.username) &&
                        password.value.equals(savedUser.value?.password)
                    ) {
                        navController.navigate("home")
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
        Text(text = savedUser.value?.username + " " + savedUser.value?.password + " " + savedUser.value?.pincode)
//        Text(text = userName.value + " " + password)
//        Text(text = shit.toString())
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
        modifier = Modifier.height(10.dp)
    )
    ClickableText(
        modifier = Modifier.fillMaxWidth(),
        text = AnnotatedString("Login using user credentials"),
        onClick = {
            togglePinCodeLogin(isPinCodeLogin)
        },
        style = TextStyle(color = Color.White, textAlign = TextAlign.Center)
    )
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
fun UserNamePasswordOption(
    userName: MutableState<String>,
    password: MutableState<String>,
    isPinCodeLogin: MutableState<Boolean>
) {
    OutlinedTextField(
        modifier = Modifier.fillMaxWidth(),
        value = userName.value,
        onValueChange = { text -> userName.value = text },
        label = { Text(text = "Username") },
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
//    IconButton(onClick = { togglePinCodeLogin(isPinCodeLogin) }) {
//        Icon(
//            painter = rememberVectorPainter(image = Icons.Default.Lock),
//            contentDescription = ""
//        )
//    }
    Spacer(
        modifier = Modifier.height(10.dp)
    )
    ClickableText(
        modifier = Modifier.fillMaxWidth(),
        text = AnnotatedString("Login using PIN code"),
        onClick = {
            togglePinCodeLogin(isPinCodeLogin)
        },
        style = TextStyle(color = Color.White, textAlign = TextAlign.Center)
    )
}

fun togglePinCodeLogin(pinCodeLogin: MutableState<Boolean>) {
    pinCodeLogin.value = !pinCodeLogin.value
}
