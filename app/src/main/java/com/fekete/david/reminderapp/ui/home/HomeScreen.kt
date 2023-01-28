package com.fekete.david.reminderapp.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.fekete.david.reminderapp.R

@Composable
fun HomeScreen(
    modifier: Modifier,
    navController: NavController
) {
    Surface() {
        HomeContent(
            navController = navController
        )
//        Column(
//            modifier = modifier.fillMaxHeight(),
//            horizontalAlignment = Alignment.CenterHorizontally,
//            verticalArrangement = Arrangement.SpaceBetween
//        ) {
//            LazyColumn(
//                modifier = Modifier
//                    .fillMaxHeight(0.92f)
//                    .fillMaxWidth()
//                    .background(color = Color.Yellow),
//                horizontalAlignment = Alignment.CenterHorizontally,
//                verticalArrangement = Arrangement.Center
//            ) {
//                Lazy("This is the home screen bitches")
//            }
//            Row(
//                modifier = Modifier
////                    .fillMaxHeight(0.1f)
//                    .fillMaxWidth()
////                    .padding(5.dp)
//                    .background(color = Color.Red),
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.Center
//            ) {
////        Text(text = "Profile")
//                IconButton(onClick = { navController.navigate("profile") }) {
//                    Icon(
//                        painter = rememberVectorPainter(image = Icons.Filled.Person),
//                        contentDescription = "",
//                        modifier = Modifier.size(35.dp)
//                    )
//                }
//            }

    }
}


@Composable
fun HomeContent(
    navController: NavController
) {
    Scaffold(
        modifier = Modifier.padding(20.dp),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /*TODO*/ },
                modifier = Modifier.padding(all = 20.dp),
                contentColor = Color.Black,
                backgroundColor = Color.Red
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null)
            }
        }
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            val appBarColor = MaterialTheme.colors.surface.copy(alpha = 0.87f)
            HomeAppBar(backgroundColor = appBarColor, navController = navController)
        }
    }
}

@Composable
private fun HomeAppBar(backgroundColor: Color, navController: NavController) {
    TopAppBar(
        title = {
            Text(
                text = stringResource(R.string.app_name),
                color = MaterialTheme.colors.secondary,
                modifier = Modifier
                    .padding(start = 4.dp)
                    .height(24.dp)
            )
        },
        backgroundColor = backgroundColor,
        actions = {
            IconButton(onClick = { navController.navigate("profile") }) {
                Icon(
                    painter = rememberVectorPainter(image = Icons.Filled.Person),
                    contentDescription = stringResource(R.string.account),
                    modifier = Modifier.size(35.dp)
                )
            }
        }
    )

}
