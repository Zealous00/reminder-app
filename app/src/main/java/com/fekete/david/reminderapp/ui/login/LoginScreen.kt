package com.fekete.david.reminderapp.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp

@Composable
fun LoginScreen(modifier: Modifier) {
    val userName = remember { mutableStateOf("") }
    val password = remember { mutableStateOf("") }
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
            modifier = Modifier.fillMaxWidth().size(150.dp),
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
            value = password.value,
            onValueChange = { pwd -> password.value = pwd },
            label = { Text(text = "Password") },
            visualTransformation = PasswordVisualTransformation(),
            shape = RoundedCornerShape(corner = CornerSize(50.dp))
        )
        Spacer(
            modifier = Modifier.height(30.dp)
        )
        Button(
            onClick = { /*TODO*/ },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(corner = CornerSize(50.dp))
        ) {
            Text(text = "Login")
        }
    }
}