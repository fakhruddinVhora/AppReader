package com.example.appreader.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.appreader.components.FabContent
import com.example.appreader.components.ListCard
import com.example.appreader.components.ReaderAppBar
import com.example.appreader.components.TitleSection
import com.example.appreader.model.MBook
import com.example.appreader.navigation.ReaderScreens
import com.google.firebase.auth.FirebaseAuth

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ReaderHomeScreen(navController: NavHostController) {
    Scaffold(topBar = {
        ReaderAppBar(title = "A. Reader", navController = navController)
    }, floatingActionButton = {
        FabContent {

        }
    }) {
        Surface(Modifier.fillMaxSize()) {
            HomeContent(navController = navController)
        }
    }
}

@Composable
fun HomeContent(navController: NavHostController) {

    val listOfBooks = listOf(
        MBook("1", "Book 1", "Author 1", "Notes 1"),
        MBook("2", "Book 2", "Author 2", "Notes 2"),
        MBook("3", "Book 3", "Author 3", "Notes 3")
    )
    val currentUser: String = if (!FirebaseAuth.getInstance().currentUser?.email.isNullOrEmpty()) {
        FirebaseAuth.getInstance().currentUser?.email?.split("@")?.get(0).toString()
    } else {
        "N/A"
    }
    Column(
        modifier = Modifier.padding(2.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            TitleSection(label = "Your Reading \n " + "Activity Right Now")
            Spacer(modifier = Modifier.width(150.dp))
            Column(modifier = Modifier.wrapContentWidth()) {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "Profile",
                    modifier = Modifier
                        .clickable {
                            navController.navigate(ReaderScreens.StatsScreen.name)
                        }
                        .size(45.dp),
                    tint = MaterialTheme.colors.secondaryVariant
                )
                Text(
                    text = currentUser,
                    modifier = Modifier
                        .padding(2.dp)
                        .wrapContentWidth(),
                    color = Color.Red,
                    maxLines = 1,
                    overflow = TextOverflow.Clip,
                )
                Divider()

            }
        }
        ReadingRightNowArea(books = listOf(), navController = navController)
        TitleSection(label = "Reading List")
        BookListArea(books = listOfBooks, navController = navController)
    }
}

@Composable
fun BookListArea(books: List<MBook>, navController: NavHostController) {
    HorizontalScrollableComponent(books, navController) {

    }
}

@Composable
fun HorizontalScrollableComponent(
    books: List<MBook>,
    navController: NavHostController,
    onCardPressed: (String?) -> Unit
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(280.dp)
            .horizontalScroll(scrollState)
    ) {
        for (book in books) {
            ListCard(book) {
                onCardPressed(it)
            }
        }
    }
}

@Composable
fun ReadingRightNowArea(books: List<MBook>, navController: NavHostController) {
    ListCard()
}

