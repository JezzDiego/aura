package com.example.aura.presentation.ui.feature_home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import com.example.aura.R
import com.example.aura.presentation.ui.components.PrimaryButton
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    var name by remember { mutableStateOf("Jessé Oliveira") }
    val pagerItems = listOf(
        R.drawable.ic_launcher_background,
        R.drawable.ic_launcher_foreground
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Aura") },
                actions = {
                    Icon(
                        imageVector = Icons.Rounded.Home,
                        contentDescription = "Home Icon",
                    )
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                containerColor = Color.Gray,
                contentColor = Color.White,
                tonalElevation = TopAppBarDefaults.TopAppBarExpandedHeight
            ) {
                Text(
                    text = "Bottom App Bar",

                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Surface(
                    modifier = Modifier
                    .size(100.dp)
                    .weight(6f),
                    color = Color.LightGray
                ) {  }
                Surface(
                    modifier = Modifier
                    .size(100.dp)
                    .weight(4f),
                    color = Color.White
                ) {  }
            }
            Text(name)
            PrimaryButton(text = "Action") {
                name = "Button Clicked"
            }

            Carrousel(pagerItems)
            Carrousel(pagerItems)
        }
    }
}

@Composable
fun Carrousel(items: List<Int>) {
    val pagerState = rememberPagerState {
        items.size
    }
    val coroutineScope = rememberCoroutineScope()

    HorizontalPager(
        state = pagerState,
        pageSize = PageSize.Fill,
        contentPadding = PaddingValues(32.dp),
    ) {page ->
        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(items[page]),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(
                        shape = RoundedCornerShape(30.dp)
                    )
                    .padding(5.dp),
                contentScale = ContentScale.Crop
            )

            Box(
                modifier = Modifier
                    .matchParentSize()
                    .padding(bottom = 10.dp)
            ) {
                Text(
                    text = "Page: $page",
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .background(
                            color = Color.Black.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(vertical = 2.dp, horizontal = 8.dp),
                    color = Color.White,
                    textAlign = TextAlign.Center,

                    )
            }
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
    ) {
        repeat(pagerState.pageCount) {index ->
            val targetWidth = if (pagerState.currentPage == index) 25.dp else 10.dp
            val animatedWidth by animateDpAsState(targetValue = targetWidth)

            val targetColor = if (pagerState.currentPage == index) Color.LightGray else Color.Gray
            val animatedColor by animateColorAsState(targetValue = targetColor)

            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .height(10.dp)
                    .width(animatedWidth)
                    .background(color = animatedColor, shape = RoundedCornerShape(50))
                    .clickable(
                        onClick = {
                            coroutineScope.launch {
                                pagerState.animateScrollToPage(
                                    page = index,
                                    animationSpec = tween(
                                        durationMillis = 300,
                                        easing = FastOutSlowInEasing
                                    )
                                )
                            }
                        }
                    )
            )
        }
    }
}

@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen()
}