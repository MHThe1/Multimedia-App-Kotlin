package com.classwork.multimediaapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.classwork.multimediaapp.ui.theme.MultimediaAppTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Screen()
        }
    }
}

@Composable
fun Screen(modifier: Modifier = Modifier) {
    val drawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    ModalNavigationDrawer(drawerContent = {
        ModalDrawerSheet {
            DrawerContent()
        }
    }, drawerState = drawerState)
    {
        Scaffold(
            topBar = {
                TopBar(
                    onOpenDrawer = {
                        scope.launch {
                            drawerState.apply {
                                if (isClosed) open() else close()
                            }
                        }
                    }
                )
            }
        ) { padding ->
            ScreenContent(modifier = Modifier.padding(padding))
        }
    }
}

@Composable
fun DrawerContent(modifier: Modifier = Modifier) {

}

@Composable
fun ScreenContent(modifier: Modifier = Modifier) {

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(onOpenDrawer: () -> Unit) {
    TopAppBar(
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.primary,
        ),
        navigationIcon = {
            Icon(
                imageVector = Icons.Default.Menu,
                contentDescription = "Menu",
                modifier = Modifier
                    .padding(start = 16.dp, end = 8.dp)
                    .size(30.dp)
                    .clickable { onOpenDrawer() }
            )
        },
        title = {
            Text(text = "Multimedia App")
        },
        actions = {
            Icon(
                imageVector = Icons.Default.Notifications,
                contentDescription = "Menu",
                modifier = Modifier
                    .size(30.dp)
            )
            Icon(
                imageVector = Icons.Default.AddCircle,
                contentDescription = "Menu",
                modifier = Modifier
                    .padding(start = 8.dp, end = 16.dp)
                    .size(30.dp)
            )
        }
        )
}

@Preview(showBackground = true)
@Composable
fun ScreenPreview() {
    MultimediaAppTheme {
        Screen()
    }
}