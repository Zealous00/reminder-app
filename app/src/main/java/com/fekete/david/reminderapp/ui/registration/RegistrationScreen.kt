package com.fekete.david.reminderapp.ui.registration

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.fekete.david.reminderapp.data.entitiy.User
import com.fekete.david.reminderapp.ui.login.StoreUserCredentials
import com.fekete.david.reminderapp.ui.profile.saveUser
import kotlinx.coroutines.launch

@Composable
fun RegistrationScreen(
    modifier: Modifier,
    navController: NavHostController,
    context: ProvidableCompositionLocal<Context>,
    onBackPress: () -> Unit
) {
    val userName = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
    val passwordAgain = remember { mutableStateOf("") }
    val phoneNumber = remember { mutableStateOf("") }
    val pincode = remember { mutableStateOf("") }
    val pinCodeSize = 4

    val localContext = LocalContext.current
    val scope = rememberCoroutineScope()
    val dataStore = StoreUserCredentials(context = localContext)

    Surface() {
        Column(
        ) {
            TopAppBar() {
                IconButton(
                    onClick = onBackPress
                ) {
                    Icon(
                        painter = rememberVectorPainter(image = Icons.Default.ArrowBack),
                        contentDescription = ""
                    )
                }
                Text(text = "Registration")
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    painter = rememberVectorPainter(image = Icons.Filled.Person),
                    contentDescription = "login_image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(100.dp),
                )
                Spacer(
                    modifier = Modifier.height(10.dp)
                )
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
                    value = phoneNumber.value,
                    onValueChange = { pn -> phoneNumber.value = pn },
                    label = { Text(text = "Phone number") },
                    shape = RoundedCornerShape(corner = CornerSize(50.dp)),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
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
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = passwordAgain.value,
                    onValueChange = { pwd -> passwordAgain.value = pwd },
                    label = { Text(text = "Confirm password") },
                    visualTransformation = PasswordVisualTransformation(),
                    shape = RoundedCornerShape(corner = CornerSize(50.dp))
                )
                Spacer(
                    modifier = Modifier.height(10.dp)
                )
                Text(text = "Pin code:", textAlign = TextAlign.Center)
                Spacer(
                    modifier = Modifier.height(4.dp)
                )
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
                    modifier = Modifier.height(16.dp)
                )
                Button(
                    onClick = {
                        if (!userName.value.isEmpty() &&
                            !password.value.isEmpty() &&
                            !passwordAgain.value.isEmpty() &&
                            !phoneNumber.value.isEmpty() &&
                            !pincode.value.isEmpty()
                        ) {
                            if (password.value.equals(passwordAgain.value)) {
                                scope.launch {
                                    dataStore.saveUserCredentials(
                                        User(
                                            username = userName.value,
                                            password = password.value,
                                            phoneNumber = phoneNumber.value,
                                            pincode = pincode.value,
                                            imageUri = ""
                                        )
                                    )
                                }
                                Toast.makeText(
                                    localContext,
                                    "Registration successful!",
                                    Toast.LENGTH_SHORT
                                ).show()
                                navController.navigate("home")
                            } else {
                                Toast.makeText(
                                    localContext,
                                    "The provided passwords do not match!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            Toast.makeText(
                                localContext,
                                "Please fill out all the fields!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(corner = CornerSize(50.dp))
                ) {
                    Text(text = "Register")
                }
            }
        }
    }
}

@Composable
fun PinCodeChar(index: Int, text: String) {
    val char = when {
        index >= text.length -> ""
        else -> text[index].toString()
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