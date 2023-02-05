package com.fekete.david.reminderapp.ui.profile

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.Person
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.fekete.david.reminderapp.R
import com.fekete.david.reminderapp.data.entitiy.User
import com.fekete.david.reminderapp.ui.login.StoreUserCredentials
import org.w3c.dom.Text

@Composable
fun ProfileScreen(
    modifier: Modifier,
    onBackPress: () -> Unit
) {
    Surface() {
        Column {
            TopAppBar() {
                IconButton(onClick = onBackPress) {
                    Icon(
                        painter = rememberVectorPainter(image = Icons.Default.ArrowBack),
                        contentDescription = ""
                    )
                }
                Text(text = "Profile")
            }

            Column(
                modifier = Modifier.fillMaxSize(),
            ) {
                PicturePart(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = MaterialTheme.colors.primary)
                        .weight(0.5f)
                )
                Spacer(modifier = Modifier.height(8.dp))
                DataPart(
                    modifier = Modifier
                        .fillMaxWidth()
//                        .background(color = Color.Red)
                        .weight(1f)
                )
            }
        }
    }
}


@Composable
fun PicturePart(modifier: Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedButton(
            onClick = { /*TODO*/ },
            modifier = Modifier.size(150.dp),
            shape = CircleShape,
            border = BorderStroke(5.dp, Color.Black),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White)
        ) {
            Icon(
                painter = rememberVectorPainter(image = Icons.Rounded.Person),
//                            tint = Color.Black,
                contentDescription = stringResource(R.string.account),
                modifier = Modifier.size(100.dp)
            )
        }

    }
}

@Composable
fun DataPart(modifier: Modifier) {
    val context = LocalContext.current
    val dataStore = StoreUserCredentials(context = context)
    val savedUser = dataStore.getUserFromDataStore.collectAsState(initial = User("", "", "", ""))
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.4f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f),
                shape = RoundedCornerShape(corner = CornerSize(50.dp)),
                border = BorderStroke(2.dp, color = MaterialTheme.colors.primary)
            ) {
                Text(
//                color = Color.Black,
                    text = "Profile details",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
            }

        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = "Nickname",
                fontSize = 14.sp,
                modifier = Modifier.padding(4.dp)
            )
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f),
                shape = RoundedCornerShape(corner = CornerSize(50.dp)),
                border = BorderStroke(2.dp, color = MaterialTheme.colors.primary)
            ) {
                Text(
//                color = Color.Black,
                    text = savedUser.value?.username + "",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Phone number",
                fontSize = 14.sp,
                modifier = Modifier.padding(4.dp)
            )
            Card(
                modifier = Modifier
                    .fillMaxWidth(0.9f),
                shape = RoundedCornerShape(corner = CornerSize(50.dp)),
                border = BorderStroke(2.dp, color = MaterialTheme.colors.primary)
            ) {
                Text(
//                color = Color.Black,
                    text = savedUser.value?.phoneNumber + "",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
            }

        }


    }
}