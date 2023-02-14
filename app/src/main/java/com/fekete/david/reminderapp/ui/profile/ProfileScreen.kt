package com.fekete.david.reminderapp.ui.profile

import android.app.Activity
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.fekete.david.reminderapp.R
import com.fekete.david.reminderapp.data.entitiy.User
import com.fekete.david.reminderapp.ui.login.StoreUserCredentials
import com.fekete.david.reminderapp.viewmodel.AuthViewModel
import com.fekete.david.reminderapp.viewmodel.ProfileViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

@Composable
fun ProfileScreen(
    modifier: Modifier,
    navController: NavController,
    authViewModel: AuthViewModel,
    profileViewModel: ProfileViewModel,
    onBackPress: () -> Unit

) {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val context = LocalContext.current


    profileViewModel.getUserProfile(FirebaseAuth.getInstance().currentUser?.uid)

    val currentUserProfile by profileViewModel.profile.collectAsState()

    Surface() {
        Column {
            TopAppBar() {
                IconButton(
                    onClick = {}
                ) {
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
                ImagePart(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = MaterialTheme.colors.primary)
                        .weight(0.4f),
                    userProfile = currentUserProfile
                )
                Spacer(modifier = Modifier.height(8.dp))
                DataPart(
                    modifier = Modifier
                        .fillMaxWidth()
//                        .background(color = Color.Red)
                        .weight(0.6f),
//                    user = savedUser,
                    navController = navController,
                    authViewModel = authViewModel,
                    userProfile = currentUserProfile
                )
            }
        }
    }
}


@Composable
fun ImagePart(
    modifier: Modifier,
    userProfile: User?
) {
//    var imageUri = remember { dataStore.getImageUri ?: "" }
    var imageUri = rememberSaveable { mutableStateOf("") }
    val scope = rememberCoroutineScope()
    val painter = rememberAsyncImagePainter(
        if (imageUri.value == "") {
            R.drawable.ic_user
        } else {
            imageUri.value
        }
    )
//    val launcher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.GetContent()
//    ) { uri: Uri? ->
//        uri?.let {
//            scope.launch { dataStore.setImageUri(uri.toString()) }
//            imageUri.value = it.toString()
//        }
//    }
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Card(
            shape = CircleShape,
            modifier = Modifier
                .size(170.dp)
                .padding(8.dp),
        ) {
            Image(
                painter = painter,
                contentDescription = "",
                modifier = Modifier
                    .clip(CircleShape)
                    .padding(16.dp),
//                    .size(50.dp)
//                    .clickable {
//                        launcher.launch("image/*")
//                    },
                contentScale = ContentScale.Crop
            )
//            Icon(
//                painter = rememberVectorPainter(image = Icons.Filled.Person),
//                contentDescription = "login_image",
//                modifier = Modifier.padding(16.dp)
//            )
        }
//        OutlinedButton(
//            onClick = { launcher.launch("image/*") },
//            modifier = Modifier
//                .size(150.dp),
//            shape = CircleShape,
//            border = BorderStroke(5.dp, Color.Black),
//            contentPadding = PaddingValues(0.dp),
////            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.White),
//        ) {
//            Image(
//                painter = painter,
//                contentDescription = "",
//                modifier = Modifier
//                    .clip(CircleShape),
////                    .clickable { launcher.launch("image/*") },
//                contentScale = ContentScale.Crop
//            )
//        }

    }
}

@Composable
fun DataPart(
    modifier: Modifier,
    userProfile: User?,
    navController: NavController,
    authViewModel: AuthViewModel,
) {
    val activity = (LocalContext.current as? Activity)
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1.0f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.2f),
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
                    .weight(0.6f),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Email address",
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
                        text = userProfile?.email.orEmpty(),
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
                        text = userProfile?.phoneNumber.orEmpty(),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(16.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.2f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                Button(
                    onClick = {
                        authViewModel.signOutFromAccount()
                        authViewModel.restartApp(activity)
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(50.dp),
                    shape = RoundedCornerShape(corner = CornerSize(50.dp)),
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red)
                ) {
                    Text(text = "Logout")
                }
                Spacer(modifier = Modifier.height(16.dp))
            }


        }
    }


}